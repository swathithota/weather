package com.demo.weather.service;


public class HighWindsProcessingStrategy implements WeatherProcessingStrategy{
    @Override
    public String processWeatherData() {
        return "It’s too windy, watch out!";
    }
}
