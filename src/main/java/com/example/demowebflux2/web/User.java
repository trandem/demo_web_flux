package com.example.demowebflux2.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    private String id;
    private String name;
    private int age;
    private double salary;
    private String department;
}
