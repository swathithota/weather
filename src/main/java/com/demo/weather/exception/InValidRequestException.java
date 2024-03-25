package com.demo.weather.exception;

import com.demo.weather.model.WarningType;

public class InValidRequestException extends RuntimeException{

    private WarningType warningType;
    public InValidRequestException(WarningType warningType) {
        super();
        this.warningType=warningType;
    }

    public WarningType getWarningType() {
        return warningType;
    }
}
