package com.netflix.testingdemo.lolomo.datafetchers;

public class Top10NotAvailableException extends RuntimeException {
    public Top10NotAvailableException(String country) {
        super("A top 10 is not available for the given country code '" + country + "'");
    }
}
