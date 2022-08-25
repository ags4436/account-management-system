package com.barclays.capstone.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 
 * @author Team PB3A
 * @Description Main Methods Invokes the Spring Application 
 * 
 */

@SpringBootApplication
public class App implements HandlerInterceptor {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
