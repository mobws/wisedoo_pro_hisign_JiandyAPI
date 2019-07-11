package com.wisedoo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.wisedoo"})
@RestController
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}