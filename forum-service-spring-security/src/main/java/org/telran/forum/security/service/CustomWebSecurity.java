package org.telran.forum.security.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.telran.forum.accounting.dao.AccountRepository;
import org.telran.forum.accounting.model.UserAccount;
import org.telran.forum.post.dao.ForumRepository;
import org.telran.forum.post.model.Post;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomWebSecurity {
	
	final ForumRepository forumRepository;
	final AccountRepository accountRepository;
	
	public boolean checkPostAuthor(String postID, String UserName) {
		Post post = forumRepository.findById(postID).orElse(null);
		return post != null && UserName.equalsIgnoreCase(post.getAuthor());
	}
	
	public boolean passwordExpirationChecker(String id){
		System.out.println("ID" + id);
		UserAccount userAccount = accountRepository.findById(id).orElse(null);
		if (userAccount != null) {
			LocalDateTime updateTime = userAccount.getCreationDate();
			return updateTime.isAfter(LocalDateTime.now().minusMonths(6));
		}
		return false;
	}
}
