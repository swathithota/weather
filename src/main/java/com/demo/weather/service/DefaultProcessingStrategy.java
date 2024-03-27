package com.demo.weather.service;

public class DefaultProcessingStrategy implements WeatherProcessingStrategy {
    @Override
    public String processWeatherData() {
        return "No specific advisory message for this forecast. Enjoy the weather!";
    }
}
