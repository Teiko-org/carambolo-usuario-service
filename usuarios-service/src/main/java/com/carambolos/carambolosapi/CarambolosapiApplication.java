package com.carambolos.carambolosapi;

import com.carambolos.carambolosapi.infrastructure.persistence.jpa.EnderecoRepository;
import com.carambolos.carambolosapi.system.security.EnderecoHasher;
import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

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

		setPropertyIfPresent(dotenv, "spring.datasource.username", "DB_USERNAME");
		setPropertyIfPresent(dotenv, "spring.datasource.password", "DB_PASSWORD");
		setPropertyIfPresent(dotenv, "spring.datasource.url", "DB_URL");

		setPropertyIfPresent(dotenv, "jwt.validity", "JWT_VALIDITY");
		setPropertyIfPresent(dotenv, "jwt.secret", "JWT_SECRET");

		setPropertyIfPresent(dotenv, "azure.storage.connection-string", "AZURE_STORAGE_CONNECTION_STRING");
		setPropertyIfPresent(dotenv, "azure.storage.container-name", "AZURE_STORAGE_CONTAINER_NAME");

		SpringApplication.run(CarambolosapiApplication.class, args);
	}

	private static void setPropertyIfPresent(Dotenv dotenv, String propertyName, String envVarName) {
		String value = dotenv.get(envVarName);
		if (value != null && !value.isEmpty()) {
			System.setProperty(propertyName, value);
		}
	}

	@Bean
	@ConditionalOnBean(EnderecoRepository.class)
	@SuppressWarnings("unused")
	CommandLineRunner backfillEnderecoDedupHash(EnderecoRepository enderecoRepository) {
		return args -> enderecoRepository.findAll().stream()
				.filter(e -> e.getDedupHash() == null || e.getDedupHash().isBlank())
				.forEach(e -> {
					e.setDedupHash(EnderecoHasher.computeDedupHash(e));
					enderecoRepository.save(e);
				});
	}
}