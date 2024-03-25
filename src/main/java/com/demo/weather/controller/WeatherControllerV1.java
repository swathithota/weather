package com.demo.weather.controller;


import com.demo.weather.model.WeatherRequest;
import com.demo.weather.model.WeatherResponse;
import com.demo.weather.service.WeatherServiceV1;
import com.demo.weather.validation.WeatherValidatorV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WeatherControllerV1 {

   private  static final Logger LOGGER = LoggerFactory.getLogger(WeatherControllerV1.class);

   @Autowired
   WeatherValidatorV1 weatherValidatorV1;

   @Autowired
   WeatherServiceV1 weatherServiceV1;

    @PostMapping(value = "/weather-prediction/v1/forecast",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeather(@RequestHeader(required = false)HttpHeaders httpHeaders, @RequestBody WeatherRequest weatherRequest) {

        StopWatch overAllWatch = new StopWatch();
        overAllWatch.start();

        weatherValidatorV1.isValidRequest(weatherRequest);

        ResponseEntity<WeatherResponse>  response = weatherServiceV1.getWeatherForecast(weatherRequest.getData());

        overAllWatch.stop();
        LOGGER.info("getWeather: Time Taken [{}] in ms", overAllWatch.getTotalTimeMillis());

        return response;



    }
}
