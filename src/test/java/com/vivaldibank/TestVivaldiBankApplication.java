package com.vivaldibank;

import org.springframework.boot.SpringApplication;

public class TestVivaldiBankApplication {

	public static void main(String[] args) {
		SpringApplication.from(VivaldiBankApplication::main).with(IntegrationTestConfig.class).run(args);
	}

}
