package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.jwt.Jwt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class JwtCreatorApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtCreatorApplication.class);

	@Autowired
	private OpenIdConnectTokenCreator openIdConnectTokenCreator;

	public static void main(String[] args) {
		SpringApplication.run(JwtCreatorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

        Jwt generatedToken = openIdConnectTokenCreator.generateToken(
                "hmustermann", "Hans", "Mustermann", Arrays.asList("USER", "ADMIN"));

        LOGGER.info("");
        LOGGER.info("---- Token ---");
        LOGGER.info(generatedToken.toString());
        LOGGER.info("Base64 encoded:");
        LOGGER.info(generatedToken.getEncoded());
        LOGGER.info("--------------");
        LOGGER.info("");
    }
}
