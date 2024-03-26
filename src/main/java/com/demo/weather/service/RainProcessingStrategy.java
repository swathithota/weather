package com.demo.weather.service;

import org.json.JSONObject;

public class RainProcessingStrategy implements  WeatherProcessingStrategy{
    @Override
    public String processWeatherData() {
        return "Carry an umbrella.";
    }
}
