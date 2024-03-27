package com.demo.weather.controller

import com.demo.weather.model.Data
import com.demo.weather.model.MaxRecordsDetails
import com.demo.weather.model.ServiceAttributes
import com.demo.weather.model.WeatherDTO
import com.demo.weather.model.WeatherRequest
import com.demo.weather.model.WeatherResponse
import com.demo.weather.service.WeatherServiceV1
import com.demo.weather.validation.WeatherValidatorV1
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class WeatherControllerV1Test extends Specification{


    WeatherValidatorV1 weatherValidatorV1 = Mock();


    WeatherServiceV1 weatherServiceV1 = Mock();

    WeatherControllerV1 weatherControllerV1 = new WeatherControllerV1();

    def "Valid Response" (){
        given:
'Create a new WeatherReading object'
        WeatherRequest weatherRequest = new WeatherRequest()

        ServiceAttributes serviceAttributes = new ServiceAttributes()
        serviceAttributes.setLocation("London")

        MaxRecordsDetails maxRecordsDetails = new MaxRecordsDetails()
        maxRecordsDetails.setMaxCount(4)
        Data data = new Data()
        data.setServiceAttributes(serviceAttributes)
        data.setMaxRecordsDetails(maxRecordsDetails)

        weatherRequest.setData(data)


        weatherControllerV1.weatherServiceV1 = weatherServiceV1
        weatherControllerV1.weatherValidatorV1 = weatherValidatorV1

        HttpHeaders httpHeaders = new HttpHeaders()

        List<WeatherDTO> weatherDTOList = new ArrayList<>()
        weatherDTOList.add(new WeatherDTO("London","28","28","Sunny"))

        WeatherResponse we = new WeatherResponse()
        we.setWeatherDTOList(weatherDTOList)
        ResponseEntity<WeatherResponse>  responseEntity = new ResponseEntity<>(we,HttpStatus.OK)
        weatherServiceV1.getWeatherForecast(*_)>> responseEntity
        when:

        ResponseEntity<WeatherResponse> response= weatherControllerV1.getWeather(httpHeaders,weatherRequest)

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody().getWeatherDTOList().size() == 1
        response.getBody().getWeatherDTOList().get(0).getAdvisoryMessage() == "Sunny"

    }

    def "InValid Response null" (){
        given:
        'Create a new WeatherReading object'
        WeatherRequest weatherRequest = new WeatherRequest()

        ServiceAttributes serviceAttributes = new ServiceAttributes()
        serviceAttributes.setLocation("London")

        MaxRecordsDetails maxRecordsDetails = new MaxRecordsDetails()
        maxRecordsDetails.setMaxCount(4)
        Data data = new Data()
        data.setServiceAttributes(serviceAttributes)
        data.setMaxRecordsDetails(maxRecordsDetails)

        weatherRequest.setData(data)


        weatherControllerV1.weatherServiceV1 = weatherServiceV1
        weatherControllerV1.weatherValidatorV1 = weatherValidatorV1

        HttpHeaders httpHeaders = new HttpHeaders()


        weatherServiceV1.getWeatherForecast(*_)>> null
        when:

        ResponseEntity<WeatherResponse> response= weatherControllerV1.getWeather(httpHeaders,weatherRequest)

        then:
        response== null
    }

}
