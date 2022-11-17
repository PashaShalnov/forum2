package org.telran.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telran.forum.accounting.dao.AccountRepository;
import org.telran.forum.accounting.model.UserAccount;

@SpringBootApplication
public class ForumServiceSpringSecurityApplication {
	
	@Autowired
	AccountRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ForumServiceSpringSecurityApplication.class, args);
	}
	
}
