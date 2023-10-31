package com.proyecto.integrador;

import com.proyecto.integrador.auth.AuthenticationRequest;
import com.proyecto.integrador.auth.AuthenticationService;
import com.proyecto.integrador.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static com.proyecto.integrador.user.Role.ADMIN;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ProyectoIntegradorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoIntegradorApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			try {
				var admin = RegisterRequest.builder()
						.firstname("Admin")
						.lastname("Mica")
						.email("mica@mail.com")
						.password("password")
						.role(ADMIN)
						.build();
				System.out.println("Admin token: " + service.rootRegister(admin).getAccessToken());
			} catch (Exception e) {
				try {
					System.out.println("Admin already registered, try to login");
					AuthenticationRequest admin = AuthenticationRequest.builder()
							.email("mica@mail.com")
							.password("password")
							.build();
					System.out.println("Admin token: " + service.login(admin).getAccessToken());
				} catch (Exception ex) {
					System.out.println("Admin login failed, check the credentials");
				}
			}
		};
	}
}
