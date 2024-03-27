package com.demo.weather.validation

import com.demo.weather.exception.CustomRequestException
import com.demo.weather.model.Data
import com.demo.weather.model.MaxRecordsDetails
import com.demo.weather.model.ServiceAttributes
import com.demo.weather.model.WeatherRequest
import spock.lang.Specification

class WeatherValidatorV1Test extends Specification{

    def "test validate"(){
        given:
        WeatherValidatorV1 weatherValidatorV1 = new WeatherValidatorV1()
        WeatherRequest weatherRequest = new WeatherRequest()

        ServiceAttributes serviceAttributes = new ServiceAttributes()
        serviceAttributes.setLocation("London")

        MaxRecordsDetails maxRecordsDetails = new MaxRecordsDetails()
        maxRecordsDetails.setMaxCount(4)
        Data data = new Data()
        data.setServiceAttributes(serviceAttributes)
        data.setMaxRecordsDetails(maxRecordsDetails)

        weatherRequest.setData(data)


        when:
       weatherValidatorV1.isValidRequest(weatherRequest)

        then:
        noExceptionThrown()
    }
    def "test invalid request"(){
        given:
        WeatherValidatorV1 weatherValidatorV1 = new WeatherValidatorV1()
        WeatherRequest weatherRequest = null


        when:
        weatherValidatorV1.isValidRequest(weatherRequest)

        then:
        thrown(CustomRequestException)
    }
    def "test invalid location"(){
        given:
        WeatherValidatorV1 weatherValidatorV1 = new WeatherValidatorV1()
        WeatherRequest weatherRequest = new WeatherRequest()

        ServiceAttributes serviceAttributes = new ServiceAttributes()
        serviceAttributes.setLocation(location)

        MaxRecordsDetails maxRecordsDetails = new MaxRecordsDetails()
        maxRecordsDetails.setMaxCount(4)
        Data data = new Data()
        data.setServiceAttributes(serviceAttributes)
        data.setMaxRecordsDetails(maxRecordsDetails)

        weatherRequest.setData(data)


        when:
        weatherValidatorV1.isValidRequest(weatherRequest)

        then:
        thrown(CustomRequestException)

        where:
        location | _
        null     | _
        ""       | _
    }
    def "test invalid maxCount"(){
        given:
        WeatherValidatorV1 weatherValidatorV1 = new WeatherValidatorV1()
        WeatherRequest weatherRequest = new WeatherRequest()

        ServiceAttributes serviceAttributes = new ServiceAttributes()
        serviceAttributes.setLocation("london")

        MaxRecordsDetails maxRecordsDetails = new MaxRecordsDetails()
        maxRecordsDetails.setMaxCount(-4)
        Data data = new Data()
        data.setServiceAttributes(serviceAttributes)
        data.setMaxRecordsDetails(maxRecordsDetails)

        weatherRequest.setData(data)


        when:
        weatherValidatorV1.isValidRequest(weatherRequest)

        then:
        thrown(CustomRequestException)


    }
}
