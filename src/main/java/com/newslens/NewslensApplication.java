package com.newslens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * {@code @ConfigurationPropertiesScan} — {@code @ConfigurationProperties} 를 붙인 record/클래스
 * (예: {@link com.newslens.collect.GuardianProperties})를 자동으로 찾아 빈으로 등록한다.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class NewslensApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewslensApplication.class, args);
	}

}
