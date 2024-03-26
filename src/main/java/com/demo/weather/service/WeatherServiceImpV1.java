package com.demo.weather.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class WeatherServiceImpV1 {

    private WeatherProcessingStrategy weatherProcessingStrategy;

    public void setWeatherProcessingStrategy(WeatherProcessingStrategy weatherProcessingStrategy) {
        this.weatherProcessingStrategy = weatherProcessingStrategy;
    }
    public String processWeatherData(){
        return  weatherProcessingStrategy.processWeatherData();
    }
}
