package com.alekshuchlyj.paging.repository;

import com.alekshuchlyj.paging.model.Employee;
import com.alekshuchlyj.paging.model.EmployeePage;
import com.alekshuchlyj.paging.model.EmployeeSearchCriteria;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class EmployeeCriteriaRepository {

    // EntityManager is an API that manages the lifecycle of entity instances
    private final EntityManager entityManager;
    // CriteriaBuilder constructs CriteriaQuery objects and their expressions
    private final CriteriaBuilder criteriaBuilder;

    public EmployeeCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Employee> findAllWithFiltersAndOrder(EmployeePage employeePage,
                                             EmployeeSearchCriteria employeeSearchCriteria){
        // CriteriaQuery is used to create a query object
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        // Root specifies the domain objects from which the query result is obtained
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        // Predicate is used to filter a query result
        Predicate predicate = getPredicate(employeeSearchCriteria, employeeRoot);
        criteriaQuery.where(predicate);
        setOrder(employeePage, criteriaQuery, employeeRoot);

        // TypedQuery is based on the criteriaQuery
        TypedQuery<Employee> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(employeePage.getPageNumber() * employeePage.getPageSize());
        typedQuery.setMaxResults(employeePage.getPageSize());

        Pageable pageable = getPageable(employeePage);

        // Getting total count employees using predicate variable
        long employeesCount = getEmployeesCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, employeesCount);
    }

    private Predicate getPredicate(EmployeeSearchCriteria employeeSearchCriteria,
                                   Root<Employee> employeeRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(employeeSearchCriteria.getFirstName())){
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get("firstName"),
                            "%" + employeeSearchCriteria.getFirstName() + "%")
            );
        }
        if (Objects.nonNull(employeeSearchCriteria.getLastName())){
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get("lastName"),
                            "%" + employeeSearchCriteria.getLastName() + "%")
            );
        }
        // Merging predicated into one predicate using AND method
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(EmployeePage employeePage,
                          CriteriaQuery<Employee> criteriaQuery,
                          Root<Employee> employeeRoot) {
        if (employeePage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get(employeePage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get(employeePage.getSortBy())));
        }
    }

    private Pageable getPageable(EmployeePage employeePage) {
        Sort sort = Sort.by(employeePage.getSortDirection(), employeePage.getSortBy());
        return PageRequest.of(employeePage.getPageNumber(),employeePage.getPageSize(), sort);
    }

    private long getEmployeesCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Employee> countRoot = countQuery.from(Employee.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
