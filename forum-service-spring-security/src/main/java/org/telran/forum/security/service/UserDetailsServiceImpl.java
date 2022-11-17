package org.telran.forum.security.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.telran.forum.accounting.dao.AccountRepository;
import org.telran.forum.accounting.model.UserAccount;

import lombok.RequiredArgsConstructor;

//АУТЕНТИФИКАЦИЯ 
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	final AccountRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = repository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
		String[] roles = userAccount.getRoles().stream()
											.map(r -> "ROLE_" + r.toUpperCase())
											.toArray(String[]::new);
		return new User(username, userAccount.getPassword(), AuthorityUtils.createAuthorityList(roles));
	}
}
