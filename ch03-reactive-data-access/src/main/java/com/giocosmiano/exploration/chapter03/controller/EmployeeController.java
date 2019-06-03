package com.giocosmiano.exploration.chapter03.controller;

import com.giocosmiano.exploration.chapter03.domain.Employee;
import com.giocosmiano.exploration.chapter03.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private static final String API_BASE_PATH = "/api";

    @GetMapping(API_BASE_PATH + "/employees")
    Flux<Employee> employees() {
        return employeeRepository.findAll();
    }

    @PostMapping(API_BASE_PATH + "/employees")
    Mono<Void> create(@RequestBody Flux<Employee> employees) {
        return employees
                .map(employee -> {
                    log.info("We will save " + employee +
                            " to a Reactive database soon!");
                    return employee;
                })
                .then();
    }

}
