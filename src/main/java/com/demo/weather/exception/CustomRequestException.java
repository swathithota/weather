package com.demo.weather.exception;

import com.demo.weather.model.WarningType;

public class CustomRequestException extends RuntimeException{

    private WarningType warningType;
    public CustomRequestException(WarningType warningType) {
        super();
        this.warningType=warningType;
    }

    public WarningType getWarningType() {
        return warningType;
    }
}
