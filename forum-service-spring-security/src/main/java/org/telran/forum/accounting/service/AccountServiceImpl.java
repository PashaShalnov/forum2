package org.telran.forum.accounting.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telran.forum.accounting.dao.AccountRepository;
import org.telran.forum.accounting.dto.NewUserDto;
import org.telran.forum.accounting.dto.RolesRespDto;
import org.telran.forum.accounting.dto.UpdateUserDto;
import org.telran.forum.accounting.dto.UserDto;
import org.telran.forum.accounting.dto.exceptions.UserExistsException;
import org.telran.forum.accounting.dto.exceptions.UserNotFoundException;
import org.telran.forum.accounting.model.UserAccount;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	AccountRepository repository;
	final AccountRepository accountRepository;
	final ModelMapper modelMapper;
	final PasswordEncoder passwordEncoder;
	
	public void run(String... args) {
		if(!repository.existsById("admin")) {
			String password = passwordEncoder.encode("admin");
			UserAccount userAccount = new UserAccount("admin", password, "", "");
			userAccount.addRole("MODERATOR");
			userAccount.addRole("ADMINISTRATOR");
			repository.save(userAccount);
		}
	}
	
	@Override
	public UserDto addUser(NewUserDto newUserDto) {
		if (accountRepository.existsById(newUserDto.getLogin())) {
			throw new UserExistsException(newUserDto.getLogin());
		}
		UserAccount userAccount = modelMapper.map(newUserDto, UserAccount.class);
		String password = passwordEncoder.encode(newUserDto.getPassword());
		userAccount.setPassword(password);
		accountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto findUser(String login) {
		UserAccount userAccount = accountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		UserAccount user = accountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		accountRepository.deleteById(login);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UpdateUserDto updateUserDto) {
		UserAccount user = accountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		if(updateUserDto.getFirstName() != null ) {
			user.setFirstName(updateUserDto.getFirstName());
		} if (updateUserDto.getLastName() != null) {
			user.setLastName(updateUserDto.getLastName());
		}
		accountRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public RolesRespDto changeRole(String login, String role, boolean addRole) {
		UserAccount userAccount = accountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		if (addRole) {
			userAccount.addRole(role.toUpperCase());
		} else {
			userAccount.removeRole(role.toUpperCase());
		}
		accountRepository.save(userAccount);	
		return modelMapper.map(userAccount, RolesRespDto.class);
	}

	@Override
	public void changePassword(String login, String password) {
		UserAccount user = accountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		String newpassword = passwordEncoder.encode(password);
		user.setPassword(newpassword);
		user.setDateWhenPasswordUpdated(LocalDateTime.now());
		accountRepository.save(user);
	}
}
