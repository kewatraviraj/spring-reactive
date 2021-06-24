package com.example.springreactivedemo.service;

import java.util.List;

import com.example.springreactivedemo.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveDemoService {

	public void initializeUsers(List<User> users);

	public Flux<User> getAllUsers();

	public Flux<User> fetchUsers();

	public void createUser(User user);

	public Mono<User> findById(Integer id);

	public Flux<User> findByName(String name);

	public Flux<User> findBySalary(Long salary);

	public Mono<User> updateUser(Integer id, User user);

}
