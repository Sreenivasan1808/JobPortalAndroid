package com.example.jobportal;

public class Job {
    private int id;
    private String title;
    private String salary;
    private String location;
    private String organization;

    // Constructor, getters and setters
    public Job(int id, String title, String salary, String location, String organization) {
        this.id = id;
        this.title = title;
        this.salary = salary;
        this.location = location;
        this.organization = organization;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSalary() {
        return salary;
    }

    public String getLocation() {
        return location;
    }

    public String getOrganization() {
        return organization;
    }
}
