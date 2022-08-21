package com.example.demowebflux2.web;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListUserResponse {
    private List<User> userList;
}
