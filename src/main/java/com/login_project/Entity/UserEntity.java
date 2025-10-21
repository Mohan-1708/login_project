package com.login_project.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "logindb")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String emailid;

    @Column(nullable = false)
    private String password;

    // --- ADD THIS FIELD ---
    @Column(nullable = false)
    private String role;

    // --- Existing Getters and Setters for id, emailid, password... ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmailid() { return emailid; }
    public void setEmailid(String emailid) { this.emailid = emailid; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // --- New Getter and Setter for role ---
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

