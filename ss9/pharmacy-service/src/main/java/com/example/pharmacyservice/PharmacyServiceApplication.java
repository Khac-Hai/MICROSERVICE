package com.example.pharmacyservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PharmacyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacyServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner pharmacyInformationRunner(
            @Value("${app.branch-name}") String branchName,
            @Value("${app.hotline}") String hotline) {
        return args -> {
            System.out.println("========== Pharmacy Information ==========");
            System.out.println("Branch : " + branchName);
            System.out.println("Hotline : " + hotline);
            System.out.println("==================");
        };
    }
}
