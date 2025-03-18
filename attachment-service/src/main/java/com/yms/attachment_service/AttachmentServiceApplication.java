package com.yms.attachment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AttachmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttachmentServiceApplication.class, args);
	}

}
