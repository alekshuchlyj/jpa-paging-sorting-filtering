package com.alekshuchlyj.paging.controller;

import com.alekshuchlyj.paging.model.Employee;
import com.alekshuchlyj.paging.model.EmployeePage;
import com.alekshuchlyj.paging.model.EmployeeSearchCriteria;
import com.alekshuchlyj.paging.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Page<Employee>> getEmployees(EmployeePage employeePage,
                                                       EmployeeSearchCriteria employeeSearchCriteria){
        // ResponseEntity represents an HTTP response, including headers, body, and status
        return new ResponseEntity<>(employeeService.getEmployees(employeePage, employeeSearchCriteria),
                HttpStatus.OK);
    }

    @PostMapping
    // @ResponseBody maps the HttpRequest body to a transfer or domain object,
    // enabling automatic deserialization of the inbound HttpRequest body onto a Java object
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){
        return new ResponseEntity<>(employeeService.addEmployee(employee), HttpStatus.OK);
    }
}
