package com.demo.weather.service

import spock.lang.Specification

class RainProcessingStrategyTest extends Specification{
    def  "test processData"(){
        given:
        RainProcessingStrategy rainProcessingStrategy = new RainProcessingStrategy()

        when:
        String result = rainProcessingStrategy.processWeatherData()

        then:
        result == "Carry an umbrella."
    }
}
