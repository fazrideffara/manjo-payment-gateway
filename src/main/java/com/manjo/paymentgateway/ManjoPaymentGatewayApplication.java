package com.manjo.paymentgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ManjoPaymentGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManjoPaymentGatewayApplication.class, args);
	}

}
