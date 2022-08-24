package com.barclays.capstone.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.HandlerInterceptor;

@SpringBootApplication
public class App implements HandlerInterceptor{

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
