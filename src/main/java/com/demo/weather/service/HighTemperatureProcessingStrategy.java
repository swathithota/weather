package com.demo.weather.service;

import org.json.JSONObject;

public class HighTemperatureProcessingStrategy implements WeatherProcessingStrategy{
    @Override
    public String processWeatherData() {
        return " Use sunscreen lotion.";
    }
}
