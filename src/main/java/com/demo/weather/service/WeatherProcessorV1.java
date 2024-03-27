package com.demo.weather.service;

import org.springframework.stereotype.Component;

@Component
public class WeatherProcessorV1 {

    private WeatherProcessingStrategy weatherProcessingStrategy;

    public void setWeatherProcessingStrategy(WeatherProcessingStrategy weatherProcessingStrategy) {
        this.weatherProcessingStrategy = weatherProcessingStrategy;
    }
    public String processWeatherData(){
        return  weatherProcessingStrategy.processWeatherData();
    }
}
