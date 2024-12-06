package com.project.kawaiinu;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KawaiinuApplication {

	public static void main(String[] args) {
		// TimeZone을 Asia/Seoul로 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		SpringApplication.run(KawaiinuApplication.class, args);
	}

}
