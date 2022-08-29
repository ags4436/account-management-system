package com.barclays.capstone.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 
 * @author Team PB3A
 * @Description Main Methods Invokes the Spring Application 
 * 
 */

@SpringBootApplication
public class App extends SpringBootServletInitializer{
 
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
 
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(App.class);
    }
}
