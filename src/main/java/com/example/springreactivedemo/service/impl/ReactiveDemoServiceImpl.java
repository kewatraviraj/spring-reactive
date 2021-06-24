package com.example.springreactivedemo.service.impl;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springreactivedemo.model.User;
import com.example.springreactivedemo.repository.UserRepository;
import com.example.springreactivedemo.service.ReactiveDemoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveDemoServiceImpl implements ReactiveDemoService {

	@Autowired
	private UserRepository userRepository;

	public void initializeUsers(List<User> users) {
		Flux<User> savedUsers = userRepository.saveAll(users);
		savedUsers.subscribe();
	}

	@Override
	public Mono<User> updateUser(Integer id, User user) {
		return userRepository.findById(id).flatMap(userData -> {
			userData.setName(user.getName());
			userData.setPhone(user.getPhone());
			userData.setSalary(user.getSalary());
			
			return userRepository.save(userData);
		});
	}

	public Flux<User> getAllUsers() {
		return userRepository.findAll().delayElements(Duration.ofMillis(100));
	}

	@Override
	public Flux<User> fetchUsers() {
		return userRepository.findAll();
	}

	public void createUser(User user) {
		userRepository.save(user).subscribe();
	}

	public Mono<User> findById(Integer id) {
		return userRepository.findById(id);
	}

	public Flux<User> findByName(String name) {
		return userRepository.findByName(name);
	}

	public Flux<User> findBySalary(Long salary) {
		return userRepository.findBySalaryGreaterThanEqual(salary);
	}
}
