package com.alekshuchlyj.paging.data;

import com.alekshuchlyj.paging.model.Employee;
import com.alekshuchlyj.paging.repository.EmployeeRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.jsf.FacesContextUtils;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class SampleDataLoader implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(SampleDataLoader.class);
    private final EmployeeRepository employeeRepository;
    private final Faker faker;

    public SampleDataLoader(EmployeeRepository employeeRepository,
                            Faker faker) {
        this.employeeRepository = employeeRepository;
        this.faker = faker;
    }


    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading Sample Data...");

        List<Employee> employeeList = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> new Employee(
                        faker.name().firstName(),
                        faker.name().lastName()
                ))
                .toList();

        employeeRepository.saveAll(employeeList);
    }
}
