package com.carambolos.carambolosapi;

import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "API Usuários - Carambolos",
				version = "v1",
				description = "Microserviço de Usuários, Endereços e Autenticação"
		),
		servers = {
				@Server(url = "http://localhost:8081", description = "Servidor Local")
		}
)
@SpringBootApplication
public class CarambolosapiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.filename("dev.env")
				.ignoreIfMissing()
				.load();

		System.setProperty("spring.datasource.username", dotenv.get("DB_USERNAME"));
		System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));
		System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));

		System.setProperty("jwt.validity", dotenv.get("JWT_VALIDITY"));
		System.setProperty("jwt.secret", dotenv.get("JWT_SECRET"));

		SpringApplication.run(CarambolosapiApplication.class, args);
	}
}