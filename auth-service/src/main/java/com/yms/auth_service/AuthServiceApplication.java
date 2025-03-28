package com.yms.auth_service;

import com.yms.auth_service.dto.request.RegistrationRequest;
import com.yms.auth_service.entity.Role;
import com.yms.auth_service.repository.RoleRepository;
import com.yms.auth_service.repository.UserRepository;
import com.yms.auth_service.service.abstracts.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import static com.yms.auth_service.utils.Role.ROLE_PROJECT_MANAGER;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@RequiredArgsConstructor
public class AuthServiceApplication {

	private final AuthenticationService authenticationService;
	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("ROLE_TEAM_MEMBER").isEmpty()) {
				roleRepository.save(Role.builder().name("ROLE_TEAM_MEMBER").build());
			}
			if (roleRepository.findByName("ROLE_TEAM_LEADER").isEmpty()) {
				roleRepository.save(Role.builder().name("ROLE_TEAM_LEADER").build());
			}
			if (roleRepository.findByName("ROLE_PROJECT_MANAGER").isEmpty()) {
				roleRepository.save(Role.builder().name("ROLE_PROJECT_MANAGER").build());
			}
			RegistrationRequest request = RegistrationRequest.builder()
					.firstname("Yusuf")
					.email("yusuf@gmail.com")
					.password("123456")
					.lastname("Solmaz")
					.build();

			if (userRepository.findByEmail("yusuf@gmail.com").isEmpty()) {
				authenticationService.register(request,ROLE_PROJECT_MANAGER.name());
			}
		};
	}
}
