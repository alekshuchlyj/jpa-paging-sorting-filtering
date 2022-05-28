package com.alekshuchlyj.paging;

import com.github.javafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.jsf.FacesContextUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class Paging {

	public static void main(String[] args) {
		SpringApplication.run(com.alekshuchlyj.paging.Paging.class, args);
	}

	@Bean
	Faker faker() {
		return new Faker();
	}
}
