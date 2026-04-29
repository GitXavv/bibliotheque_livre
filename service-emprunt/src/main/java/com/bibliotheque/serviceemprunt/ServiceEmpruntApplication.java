package com.bibliotheque.serviceemprunt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceEmpruntApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceEmpruntApplication.class, args);
	}
}
