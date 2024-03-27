package com.demo.weather.service

import spock.lang.Specification

class ThunderstormProcessingStrategyTest extends Specification{

    def "test processData"(){
        given:
        ThunderstormProcessingStrategy thunderstormProcessingStrategy = new ThunderstormProcessingStrategy()

        when:
        String result = thunderstormProcessingStrategy.processWeatherData()

        then:
        result == "Don't step out! A Storn is brewing!"
}
}