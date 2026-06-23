package com.example.medicaldiscoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Bai1Application {

    public static void main(String[] args) {
        SpringApplication.run(
                Bai1Application.class,
                args
        );
    }
}