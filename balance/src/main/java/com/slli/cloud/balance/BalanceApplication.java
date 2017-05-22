package com.slli.cloud.balance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BalanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalanceApplication.class, args);
	}
}
