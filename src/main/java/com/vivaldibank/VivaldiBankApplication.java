package com.vivaldibank;

import com.vivaldibank.infrastructure.config.VivaldiQueueProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(VivaldiQueueProperties.class)
public class VivaldiBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(VivaldiBankApplication.class, args);
	}

}
