package com.sung.local;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication
public class LocalGovernmentSupportInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalGovernmentSupportInfoApplication.class, args);
	}

}
