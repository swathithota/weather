package com.demo.weather.model;

public class Data {

    private ServiceAttributes serviceAttributes;
    private  MaxRecordsDetails maxRecordsDetails;

    public ServiceAttributes getServiceAttributes() {
        return serviceAttributes;
    }

    public void setServiceAttributes(ServiceAttributes serviceAttributes) {
        this.serviceAttributes = serviceAttributes;
    }

    public MaxRecordsDetails getMaxRecordsDetails() {
        return maxRecordsDetails;
    }

    public void setMaxRecordsDetails(MaxRecordsDetails maxRecordsDetails) {
        this.maxRecordsDetails = maxRecordsDetails;
    }
}
