package com.demo.weather.controller

import spock.lang.Specification

class IndexControllerTest extends Specification{

    def "Valid Response" (){
        given:
        IndexController indexController = new IndexController()
        when:
        String value=indexController.index()
        then:
        value == "index"
    }
}
