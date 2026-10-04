package com.example.auth.backend.security;

import com.example.auth.backend.entity.User;
import com.example.auth.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;
    public CustomUserDetailsService(UserRepository repository){this.repository=repository;}
    @Override public UserDetails loadUserByUsername(String username) {
        User user=repository.findByUsername(username)
            .orElseThrow(()->new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole().name())
            .disabled(!user.isEnabled()).build();
    }
}
