package com.whatsap.inc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WhatsappBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhatsappBackApplication.class, args);
	}

}
