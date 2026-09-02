package com.example.suggestion_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SuggestionSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuggestionSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepo, PasswordEncoder encoder){
		return args -> {
			if(userRepo.findByUsername("admin").isEmpty()){
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(encoder.encode("admin123"));
				admin.setRole("ADMIN");
				userRepo.save(admin);
			}

			if(userRepo.findByUsername("user1").isEmpty()){
				User user = new User();
				user.setUsername("user1");
				user.setPassword(encoder.encode("user123"));
				user.setRole("USER");
				userRepo.save(user);
			}
		};
	}
}
