package com.demo.weather.validation;

import com.demo.weather.exception.CustomRequestException;
import com.demo.weather.model.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.demo.weather.util.Util.BAD_REQUEST;

@Component
public class WeatherValidatorV1 implements RequestValidatorBase{
    @Override
    public void isValidRequest(WeatherRequest weatherRequest) {


        if(weatherRequest ==null || weatherRequest.getData()==null ){

            throw new CustomRequestException( new WarningType("Data is Null",BAD_REQUEST,"The field is Null","data"));
        }

        Data data = weatherRequest.getData();
        ServiceAttributes serviceAttributes = data.getServiceAttributes();
        MaxRecordsDetails maxRecordsDetails = data.getMaxRecordsDetails();
        if(!(StringUtils.hasLength(serviceAttributes.getLocation()))){
            throw new CustomRequestException(new WarningType("Location is Null",BAD_REQUEST,"The Location is Null","location"));
        }
        if (maxRecordsDetails.getMaxCount()<0 ) {
            throw new CustomRequestException(new WarningType("Max Record is invalid",BAD_REQUEST,"The Max Record is invalid","maxCount"));
        }

    }
}
