package com.wello.wellobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WelloBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WelloBackendApplication.class, args);
    }

}
