package com.example.auth.backend.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="users")
public class User {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    @Column(nullable=false, unique=true, length=50)
    private String username;
    @Column(nullable=false, unique=true, length=255)
    private String email;
    @Column(nullable=false, length=255)
    private String password;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20)
    private Role role = Role.USER;
    @Column(nullable=false)
    private boolean enabled = true;

    public UUID getId(){return id;}
    public String getUsername(){return username;}
    public void setUsername(String v){username=v;}
    public String getEmail(){return email;}
    public void setEmail(String v){email=v;}
    public String getPassword(){return password;}
    public void setPassword(String v){password=v;}
    public Role getRole(){return role;}
    public void setRole(Role v){role=v;}
    public boolean isEnabled(){return enabled;}
    public void setEnabled(boolean v){enabled=v;}
}
