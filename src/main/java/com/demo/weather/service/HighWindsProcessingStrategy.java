package com.demo.weather.service;

import org.json.JSONObject;

public class HighWindsProcessingStrategy implements WeatherProcessingStrategy{
    @Override
    public String processWeatherData() {
        return "It’s too windy, watch out!";
    }
}
