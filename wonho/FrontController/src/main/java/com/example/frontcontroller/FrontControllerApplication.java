package com.example.frontcontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class FrontControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontControllerApplication.class, args);
    }

}
