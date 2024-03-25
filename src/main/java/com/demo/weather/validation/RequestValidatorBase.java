package com.demo.weather.validation;

import com.demo.weather.model.WeatherRequest;

public interface RequestValidatorBase {

    void isValidRequest(WeatherRequest weatherRequest);
}
