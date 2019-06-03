package com.giocosmiano.exploration.chapter03.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Employee {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String role;

    public Employee() {}

    public Employee(String id, String firstName, String lastName, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}