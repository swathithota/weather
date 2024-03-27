package com.demo.weather.service

import spock.lang.Specification

class WeatherProcessorV1Test extends Specification{

    def "test processData"(){
        given:
        WeatherProcessorV1 weatherProcessorV1 = new WeatherProcessorV1()

        WeatherProcessingStrategy weatherProcessingStrategy = Mock(WeatherProcessingStrategy)

        weatherProcessingStrategy.processWeatherData() >>"Don't step out! A Storn is brewing!"
        when:
        weatherProcessorV1.setWeatherProcessingStrategy(weatherProcessingStrategy)
        String result = weatherProcessorV1.processWeatherData()


        then:
        result !=null
    }
    def "test processData null"(){
        given:
        WeatherProcessorV1 weatherProcessorV1 = new WeatherProcessorV1()

        WeatherProcessingStrategy weatherProcessingStrategy = Mock(WeatherProcessingStrategy)

        weatherProcessingStrategy.processWeatherData() >> null
        when:
        weatherProcessorV1.setWeatherProcessingStrategy(weatherProcessingStrategy)
        String result = weatherProcessorV1.processWeatherData()


        then:
        result ==null
    }

}
