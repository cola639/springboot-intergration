package com.springMovie.controller;

import com.springMovie.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/testAuth")
    public String testAuth() {
        return "OK";
    }


    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
        Map<String, String> response = new HashMap<>();

        if ("admin".equals(username) && passwordEncoder.matches(password, passwordEncoder.encode("admin123"))) {
            String token = jwtUtil.generateToken(username);
            response.put("token", token);
            return response;
        } else {
            response.put("error", "Invalid username or password");
            return response;
        }
    }
}
