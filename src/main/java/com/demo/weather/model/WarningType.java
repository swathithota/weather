package com.demo.weather.model;

public class WarningType {

    private String description;
    private String code;
    private String detail;
    private String field;

    public String toString() {
        return "WarningType{" +
                "description='" + description + '\'' +
                ", code='" + code + '\'' +
                ", detail='" + detail + '\'' +
                ", field='" + field + '\'' +
                '}';
    }

    public WarningType() {
    }

    public WarningType(String description, String code,  String detail, String field) {
        this.description = description;
        this.code = code;

        this.detail = detail;
        this.field = field;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
