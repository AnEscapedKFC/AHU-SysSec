package com.example.legitbackend.controller;

import com.example.legitbackend.dto.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
// 允许来自 http://localhost:8080 的跨域请求 (为了让正版前端能访问)
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        
        // 模拟验证：正确的账号密码为 admin/password123
        if ("admin".equals(loginRequest.getUsername()) && "password123".equals(loginRequest.getPassword())) {
            // 登录成功
            String sessionId = UUID.randomUUID().toString();
            
            // 设置 Cookie
            response.setHeader("Set-Cookie", "session_id=" + sessionId + "; Path=/; HttpOnly");

            // 返回成功 JSON
            Map<String, Object> body = Map.of(
                "success", true,
                "message", "登录成功！"
            );
            return ResponseEntity.ok(body);
        } else {
            // 登录失败
            Map<String, Object> body = Map.of(
                "success", false,
                "message", "账号或密码错误"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }
    }
}
