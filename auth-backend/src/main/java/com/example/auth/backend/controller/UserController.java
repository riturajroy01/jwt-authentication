package com.example.auth.backend.controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/profile")
    public Map<String,Object> profile(Authentication authentication){
        return Map.of("username",authentication.getName(),"message","Authenticated successfully");
    }
}
