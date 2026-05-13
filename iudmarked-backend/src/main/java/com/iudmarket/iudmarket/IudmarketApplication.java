package com.iudmarket.iudmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IudmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(IudmarketApplication.class, args);
	}

}
