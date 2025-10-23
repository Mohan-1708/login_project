package com.login_project.Controller;

// This is a DTO (Data Transfer Object) for the update request
public class ProfileUpdateRequest {
    private String name;
    private String mobileNumber;
    private String college;
    private String currentYear;
    private String currentSemester;

    // --- Getters and Setters ---

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