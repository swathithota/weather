package com.demo.weather.model;

public class WeatherDTO {

    private String date;
    private String maxTemperature;
    private String minTemperature;
    private String advisoryMessage;

    public WeatherDTO() {
    }

    @Override
    public String toString() {
        return "WeatherDTO{" +
                "date='" + date + '\'' +
                ", maxTemperature='" + maxTemperature + '\'' +
                ", minTemperature='" + minTemperature + '\'' +
                ", advisoryMessage='" + advisoryMessage + '\'' +
                '}';
    }

    public WeatherDTO(String date, String maxTemperature, String minTemperature, String advisoryMessage) {
        this.date = date;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.advisoryMessage = advisoryMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getAdvisoryMessage() {
        return advisoryMessage;
    }

    public void setAdvisoryMessage(String advisoryMessage) {
        this.advisoryMessage = advisoryMessage;
    }
}
