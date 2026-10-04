package com.example.auth.backend.service;

import com.example.auth.backend.dto.*;
import com.example.auth.backend.entity.*;
import com.example.auth.backend.repository.UserRepository;
import com.example.auth.backend.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository users,PasswordEncoder encoder,AuthenticationManager authenticationManager,JwtService jwtService){
        this.users=users;
        this.encoder=encoder;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }
    public void register(RegisterRequest request){
        if(users.existsByUsername(request.username())) throw new IllegalArgumentException("Username already exists");
        if(users.existsByEmail(request.email())) throw new IllegalArgumentException("Email already exists");
        User user=new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(Role.USER);
        users.save(user);
    }
    public LoginResponse login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
        User user=users.findByUsername(request.username()).orElseThrow();
        return new LoginResponse(jwtService.generateToken(user),"Bearer",jwtService.getExpirationSeconds(),user.getUsername(),user.getRole().name());
    }
}
