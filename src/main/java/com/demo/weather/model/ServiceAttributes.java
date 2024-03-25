package com.demo.weather.model;

public class ServiceAttributes {

    private String location;

    public ServiceAttributes() {
    }

    public ServiceAttributes(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
