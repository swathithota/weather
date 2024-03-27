package com.demo.weather.service

import spock.lang.Specification

class DefaultProcessingStrategyTest extends Specification{

    def "test processData"(){
        given:
        DefaultProcessingStrategy defaultProcessingStrategy = new DefaultProcessingStrategy()

        when:
        String result = defaultProcessingStrategy.processWeatherData()

        then:
        result == "No specific advisory message for this forecast. Enjoy the weather!"
    }
}
