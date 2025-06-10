package com.example.legitbackend.dto;

import lombok.Data;

@Data // Lombok 注解，自动生成 getter, setter, toString 等
public class LoginRequest {
    private String username;
    private String password;
}