package com.netflix.testingdemo;

import com.netflix.testingdemo.lolomo.config.LolomoApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LolomoApplicationConfig.class)
public class TestingDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestingDemoApplication.class, args);
    }
}
