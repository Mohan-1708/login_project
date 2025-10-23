package com.login_project.Controller;

import com.login_project.Entity.UserEntity;

// This DTO is used to safely send profile data *without* the password
public class ProfileResponse {
    private String emailid;
    private String name;
    private String mobileNumber;
    private String college;
    private String currentYear;
    private String currentSemester;

    // A handy constructor to map from the UserEntity
    public ProfileResponse(UserEntity user) {
        this.emailid = user.getEmailid();
        this.name = user.getName();
        this.mobileNumber = user.getMobileNumber();
        this.college = user.getCollege();
        this.currentYear = user.getCurrentYear();
        this.currentSemester = user.getCurrentSemester();
    }

    // --- Getters and Setters ---

    public String getEmailid() { return emailid; }
    public void setEmailid(String emailid) { this.emailid = emailid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getCurrentYear() { return currentYear; }
    public void setCurrentYear(String currentYear) { this.currentYear = currentYear; }

    public String getCurrentSemester() { return currentSemester; }
    public void setCurrentSemester(String currentSemester) { this.currentSemester = currentSemester; }
}