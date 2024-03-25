package com.demo.weather.model;

import java.util.List;

public class WeatherResponse {

    List<WeatherDTO> weatherDTOList;

    public WeatherResponse() {
    }

    public WeatherResponse(List<WeatherDTO> weatherDTOList) {
        this.weatherDTOList = weatherDTOList;
    }

    public List<WeatherDTO> getWeatherDTOList() {
        return weatherDTOList;
    }

    public void setWeatherDTOList(List<WeatherDTO> weatherDTOList) {
        this.weatherDTOList = weatherDTOList;
    }
}
