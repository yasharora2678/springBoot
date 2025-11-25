package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.demo")
public class DemoApplication {

	public static void main(String[] args) {
		// ConfigurableApplicationContext context =
		// SpringApplication.run(DemoApplication.class, args);
		// context.close();
		SpringApplication.run(DemoApplication.class, args);
	}
}
