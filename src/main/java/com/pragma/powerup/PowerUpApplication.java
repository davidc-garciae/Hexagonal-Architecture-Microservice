package com.pragma.powerup;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;

@SpringBootApplication
public class PowerUpApplication {

	public static void main(String[] args) {
		loadEnvironmentVariables();
		SpringApplication.run(PowerUpApplication.class, args);
	}

	private static void loadEnvironmentVariables() {
		try {
			File envFile = new File(".env");
			Dotenv dotenv = null;
			if (envFile.exists()) {
				dotenv = Dotenv.configure().filename(".env").load();
			}
			if (dotenv != null) {
				dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
			}
		} catch (Exception e) {
            // Si no se pueden cargar las variables, continuar sin error
		}
	}

}
