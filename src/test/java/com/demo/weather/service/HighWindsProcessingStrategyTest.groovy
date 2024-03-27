package com.demo.weather.service

import spock.lang.Specification

class HighWindsProcessingStrategyTest  extends Specification{

    def "test processData"() {
        given:
        HighWindsProcessingStrategy highWindsProcessingStrategy = new HighWindsProcessingStrategy()

        when:
        String result = highWindsProcessingStrategy.processWeatherData()

        then:
        result == "It’s too windy, watch out!"
    }

}
