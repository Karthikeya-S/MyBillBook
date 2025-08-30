package com.application.mybillbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MyBillBookApplication {

	public static void main(String[] args) {
        SpringApplication.run(MyBillBookApplication.class, args);
	}

}
