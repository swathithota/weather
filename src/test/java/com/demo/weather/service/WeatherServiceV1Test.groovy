package com.demo.weather.service

import com.demo.weather.exception.CustomRequestException
import com.demo.weather.model.Data
import com.demo.weather.model.MaxRecordsDetails
import com.demo.weather.model.ServiceAttributes
import com.demo.weather.model.WeatherDTO
import com.demo.weather.model.WeatherRequest
import com.demo.weather.model.WeatherResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class WeatherServiceV1Test extends Specification{

    def "test getWeather valid data"(){
        given:
        RestTemplate restTemplate = Mock(RestTemplate)
        WeatherProcessorV1 weatherProcessor = Mock(WeatherProcessorV1)


        WeatherServiceV1 weatherServiceV1 = new WeatherServiceV1()
        weatherServiceV1.restTemplate = restTemplate
        weatherServiceV1.weatherProcessorV1= weatherProcessor
        weatherServiceV1.API_KEY = "KEY"
        weatherServiceV1.API_URI= "URL"

        WeatherRequest weatherRequest = new WeatherRequest()

        ServiceAttributes serviceAttributes = new ServiceAttributes()
        serviceAttributes.setLocation("London")

        MaxRecordsDetails maxRecordsDetails = new MaxRecordsDetails()
        maxRecordsDetails.setMaxCount(4)
        Data data = new Data()
        data.setServiceAttributes(serviceAttributes)
        data.setMaxRecordsDetails(maxRecordsDetails)

        weatherRequest.setData(data)

        ResponseEntity<WeatherResponse> responseEntity = Mock(ResponseEntity)
        responseEntity.getBody(*_) >> responseBody
        restTemplate.exchange(*_) >> responseEntity

        weatherServiceV1.weatherProcessorV1.processWeatherData() >> message
        when:
        ResponseEntity<WeatherResponse> result = weatherServiceV1.getWeatherForecast(data)

        then:
        result!=null
        result.getBody().getWeatherDTOList().size() ==3

        where:
        responseBody|message
        "{\"cod\":\"200\",\"message\":0,\"cnt\":10,\"list\":[{\"dt\":1711962000,\"main\":{\"temp\":283.46,\"feels_like\":282.74,\"temp_min\":283.46,\"temp_max\":283.74,\"pressure\":992,\"sea_level\":992,\"grnd_level\":990,\"humidity\":84,\"temp_kf\":-0.28},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"clouds\":{\"all\":40},\"wind\":{\"speed\":3.87,\"deg\":221,\"gust\":6.8},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-01 09:00:00\"},{\"dt\":1711972800,\"main\":{\"temp\":284.29,\"feels_like\":283.44,\"temp_min\":284.29,\"temp_max\":285.96,\"pressure\":993,\"sea_level\":993,\"grnd_level\":991,\"humidity\":76,\"temp_kf\":-1.67},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":37},\"wind\":{\"speed\":4.69,\"deg\":204,\"gust\":5.83},\"visibility\":10000,\"pop\":0.3,\"rain\":{\"3h\":0.22},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-01 12:00:00\"},{\"dt\":1711983600,\"main\":{\"temp\":285.01,\"feels_like\":284.05,\"temp_min\":285.01,\"temp_max\":285.78,\"pressure\":994,\"sea_level\":994,\"grnd_level\":992,\"humidity\":69,\"temp_kf\":-0.77},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":71},\"wind\":{\"speed\":5.32,\"deg\":212,\"gust\":7.32},\"visibility\":10000,\"pop\":0.74,\"rain\":{\"3h\":0.41},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-01 15:00:00\"},{\"dt\":1711994400,\"main\":{\"temp\":284.31,\"feels_like\":283.26,\"temp_min\":284.31,\"temp_max\":284.31,\"pressure\":996,\"sea_level\":996,\"grnd_level\":993,\"humidity\":68,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":75},\"wind\":{\"speed\":4.02,\"deg\":203,\"gust\":8.48},\"visibility\":10000,\"pop\":0.37,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-01 18:00:00\"},{\"dt\":1712005200,\"main\":{\"temp\":282.33,\"feels_like\":280.37,\"temp_min\":282.33,\"temp_max\":282.33,\"pressure\":997,\"sea_level\":997,\"grnd_level\":994,\"humidity\":76,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"clouds\":{\"all\":35},\"wind\":{\"speed\":3.54,\"deg\":194,\"gust\":10.51},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2024-04-01 21:00:00\"},{\"dt\":1712016000,\"main\":{\"temp\":282.19,\"feels_like\":279.64,\"temp_min\":282.19,\"temp_max\":282.19,\"pressure\":998,\"sea_level\":998,\"grnd_level\":995,\"humidity\":84,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"clouds\":{\"all\":37},\"wind\":{\"speed\":4.74,\"deg\":207,\"gust\":10.99},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2024-04-02 00:00:00\"},{\"dt\":1712026800,\"main\":{\"temp\":282.29,\"feels_like\":279.52,\"temp_min\":282.29,\"temp_max\":282.29,\"pressure\":997,\"sea_level\":997,\"grnd_level\":994,\"humidity\":84,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":96},\"wind\":{\"speed\":5.39,\"deg\":202,\"gust\":10.89},\"visibility\":10000,\"pop\":0.52,\"rain\":{\"3h\":0.19},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2024-04-02 03:00:00\"},{\"dt\":1712037600,\"main\":{\"temp\":281.57,\"feels_like\":278.8,\"temp_min\":281.57,\"temp_max\":281.57,\"pressure\":999,\"sea_level\":999,\"grnd_level\":996,\"humidity\":84,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":96},\"wind\":{\"speed\":4.92,\"deg\":247,\"gust\":8.85},\"visibility\":10000,\"pop\":0.46,\"rain\":{\"3h\":0.19},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-02 06:00:00\"},{\"dt\":1712048400,\"main\":{\"temp\":282.93,\"feels_like\":280.95,\"temp_min\":282.93,\"temp_max\":282.93,\"pressure\":1002,\"sea_level\":1002,\"grnd_level\":999,\"humidity\":74,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":99},\"wind\":{\"speed\":3.86,\"deg\":251,\"gust\":7.76},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-02 09:00:00\"},{\"dt\":1712059200,\"main\":{\"temp\":287.45,\"feels_like\":286.35,\"temp_min\":287.45,\"temp_max\":287.45,\"pressure\":1003,\"sea_level\":1003,\"grnd_level\":1000,\"humidity\":54,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":77},\"wind\":{\"speed\":4.69,\"deg\":243,\"gust\":6.51},\"visibility\":10000,\"pop\":0.06,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-02 12:00:00\"}],\"city\":{\"id\":2643743,\"name\":\"London\",\"coord\":{\"lat\":51.5085,\"lon\":-0.1257},\"country\":\"GB\",\"population\":1000000,\"timezone\":3600,\"sunrise\":1711949696,\"sunset\":1711996397}}"|"It’s too windy, watch out!"
        "{\"cod\":\"200\",\"message\":0,\"cnt\":10,\"list\":[{\"dt\":1711962000,\"main\":{\"temp\":283.46,\"feels_like\":282.74,\"temp_min\":283.46,\"temp_max\":283.74,\"pressure\":992,\"sea_level\":992,\"grnd_level\":990,\"humidity\":84,\"temp_kf\":-0.28},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"clouds\":{\"all\":40},\"wind\":{\"speed\":3.87,\"deg\":221,\"gust\":6.8},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-01 09:00:00\"},{\"dt\":1711972800,\"main\":{\"temp\":284.29,\"feels_like\":283.44,\"temp_min\":284.29,\"temp_max\":28,\"pressure\":993,\"sea_level\":993,\"grnd_level\":991,\"humidity\":76,\"temp_kf\":-1.67},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":37},\"wind\":{\"speed\":4.69,\"deg\":204,\"gust\":5.83},\"visibility\":10000,\"pop\":0.3,\"rain\":{\"3h\":0.22},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-01 12:00:00\"},{\"dt\":1711983600,\"main\":{\"temp\":285.01,\"feels_like\":284.05,\"temp_min\":285.01,\"temp_max\":285.78,\"pressure\":994,\"sea_level\":994,\"grnd_level\":992,\"humidity\":69,\"temp_kf\":-0.77},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":71},\"wind\":{\"speed\":5.32,\"deg\":212,\"gust\":7.32},\"visibility\":10000,\"pop\":0.74,\"rain\":{\"3h\":0.41},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-01 15:00:00\"},{\"dt\":1711994400,\"main\":{\"temp\":284.31,\"feels_like\":283.26,\"temp_min\":284.31,\"temp_max\":284.31,\"pressure\":996,\"sea_level\":996,\"grnd_level\":993,\"humidity\":68,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":75},\"wind\":{\"speed\":4.02,\"deg\":203,\"gust\":8.48},\"visibility\":10000,\"pop\":0.37,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-01 18:00:00\"},{\"dt\":1712005200,\"main\":{\"temp\":282.33,\"feels_like\":280.37,\"temp_min\":282.33,\"temp_max\":282.33,\"pressure\":997,\"sea_level\":997,\"grnd_level\":994,\"humidity\":76,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"clouds\":{\"all\":35},\"wind\":{\"speed\":3.54,\"deg\":194,\"gust\":10.51},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2024-04-01 21:00:00\"},{\"dt\":1712016000,\"main\":{\"temp\":282.19,\"feels_like\":279.64,\"temp_min\":282.19,\"temp_max\":282.19,\"pressure\":998,\"sea_level\":998,\"grnd_level\":995,\"humidity\":84,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"clouds\":{\"all\":37},\"wind\":{\"speed\":4.74,\"deg\":207,\"gust\":10.99},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2024-04-02 00:00:00\"},{\"dt\":1712026800,\"main\":{\"temp\":282.29,\"feels_like\":279.52,\"temp_min\":282.29,\"temp_max\":282.29,\"pressure\":997,\"sea_level\":997,\"grnd_level\":994,\"humidity\":84,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":96},\"wind\":{\"speed\":5.39,\"deg\":202,\"gust\":10.89},\"visibility\":10000,\"pop\":0.52,\"rain\":{\"3h\":0.19},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2024-04-02 03:00:00\"},{\"dt\":1712037600,\"main\":{\"temp\":281.57,\"feels_like\":278.8,\"temp_min\":281.57,\"temp_max\":281.57,\"pressure\":999,\"sea_level\":999,\"grnd_level\":996,\"humidity\":84,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":96},\"wind\":{\"speed\":4.92,\"deg\":247,\"gust\":8.85},\"visibility\":10000,\"pop\":0.46,\"rain\":{\"3h\":0.19},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-02 06:00:00\"},{\"dt\":1712048400,\"main\":{\"temp\":282.93,\"feels_like\":280.95,\"temp_min\":282.93,\"temp_max\":282.93,\"pressure\":1002,\"sea_level\":1002,\"grnd_level\":999,\"humidity\":74,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":99},\"wind\":{\"speed\":3.86,\"deg\":251,\"gust\":7.76},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-02 09:00:00\"},{\"dt\":1712059200,\"main\":{\"temp\":287.45,\"feels_like\":286.35,\"temp_min\":287.45,\"temp_max\":287.45,\"pressure\":1003,\"sea_level\":1003,\"grnd_level\":1000,\"humidity\":54,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":77},\"wind\":{\"speed\":4.69,\"deg\":243,\"gust\":6.51},\"visibility\":10000,\"pop\":0.06,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2024-04-02 12:00:00\"}],\"city\":{\"id\":2643743,\"name\":\"London\",\"coord\":{\"lat\":51.5085,\"lon\":-0.1257},\"country\":\"GB\",\"population\":1000000,\"timezone\":3600,\"sunrise\":1711949696,\"sunset\":1711996397}}"|"Default!"


    }

    def "test getWeather null data"(){
        given:
        RestTemplate restTemplate = Mock(RestTemplate)
        WeatherProcessorV1 weatherProcessor = Mock(WeatherProcessorV1)


        WeatherServiceV1 weatherServiceV1 = new WeatherServiceV1()
        weatherServiceV1.restTemplate = restTemplate
        weatherServiceV1.weatherProcessorV1= weatherProcessor
        weatherServiceV1.API_KEY = "KEY"
        weatherServiceV1.API_URI= "URL"

        WeatherRequest weatherRequest = new WeatherRequest()

        ServiceAttributes serviceAttributes = new ServiceAttributes()
        serviceAttributes.setLocation("London")

        MaxRecordsDetails maxRecordsDetails = new MaxRecordsDetails()
        maxRecordsDetails.setMaxCount(4)
        Data data = new Data()
        data.setServiceAttributes(serviceAttributes)
        data.setMaxRecordsDetails(maxRecordsDetails)

        weatherRequest.setData(data)

        ResponseEntity<WeatherResponse> responseEntity = Mock(ResponseEntity)
        responseEntity.getBody(*_) >> null
        restTemplate.exchange(*_) >> responseEntity

        when:
        ResponseEntity<WeatherResponse> result = weatherServiceV1.getWeatherForecast(data)

        then:
        thrown(CustomRequestException.class)



    }
}
