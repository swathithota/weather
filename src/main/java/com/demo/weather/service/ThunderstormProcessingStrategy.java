package com.demo.weather.service;

import org.json.JSONObject;

public class ThunderstormProcessingStrategy implements WeatherProcessingStrategy {
    @Override
    public String processWeatherData() {
        return "Don't step out! A Storn is brewing!";
    }
}
