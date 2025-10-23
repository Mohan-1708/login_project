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



    @Column(nullable = true) // <-- Make it nullable
    private String name;

    @Column(nullable = true)
    private String mobileNumber;

    @Column(nullable = true)
    private String college;

    @Column(nullable = true)
    private String currentYear; // e.g., "1st", "2nd"

    @Column(nullable = true)
    private String currentSemester; // e.g., "3rd", "4th"

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public String getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(String currentSemester) {
        this.currentSemester = currentSemester;
    }

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

