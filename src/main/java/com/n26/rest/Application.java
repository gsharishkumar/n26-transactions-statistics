package com.n26.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot application class to boot or run the application
 *
 * @author Harish Kumar Govindaswamy Saravanam
 * @version 1.0
 * @since 2017-08-01
 */
@SpringBootApplication(scanBasePackages = { "com.n26.rest" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
