package com.demo.weather.service

import spock.lang.Specification

class HighTemperatureProcessingStrategyTest extends Specification {

    def "test processData"() {
        given:
        HighTemperatureProcessingStrategy highTemperatureProcessingStrategy = new HighTemperatureProcessingStrategy()

        when:
        String result = highTemperatureProcessingStrategy.processWeatherData()

        then:
        result == "Use sunscreen lotion."
    }
}
