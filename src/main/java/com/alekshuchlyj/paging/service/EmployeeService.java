package com.alekshuchlyj.paging.service;

import com.alekshuchlyj.paging.model.Employee;
import com.alekshuchlyj.paging.model.EmployeePage;
import com.alekshuchlyj.paging.model.EmployeeSearchCriteria;
import com.alekshuchlyj.paging.repository.EmployeeCriteriaRepository;
import com.alekshuchlyj.paging.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeCriteriaRepository employeeCriteriaRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeCriteriaRepository employeeCriteriaRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeCriteriaRepository = employeeCriteriaRepository;
    }

    public Page<Employee> getEmployees(EmployeePage employeePage,
                                       EmployeeSearchCriteria employeeSearchCriteria){
        return employeeCriteriaRepository.findAllWithFiltersAndOrder(employeePage, employeeSearchCriteria);
    }

    public Employee addEmployee(Employee employee){
        return employeeRepository.save(employee);
    }
}
