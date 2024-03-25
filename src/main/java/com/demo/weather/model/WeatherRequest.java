package com.demo.weather.model;

public class WeatherRequest {

    private Data data;

    public WeatherRequest() {
    }

    public WeatherRequest(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
