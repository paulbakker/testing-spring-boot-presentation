package com.netflix.testingdemo.lolomo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lolomo")
public class LolomoApplicationConfig {

    private boolean initializeShows;

    public boolean isInitializeShows() {
        return initializeShows;
    }

    public void setInitializeShows(boolean initializeShows) {
        this.initializeShows = initializeShows;
    }
}
