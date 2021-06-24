package com.example.springreactivedemo.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.example.springreactivedemo.model.User;

import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCassandraRepository<User, Integer> {

	@AllowFiltering
	Flux<User> findByName(String name);
	
	@AllowFiltering
	Flux<User> findBySalaryGreaterThanEqual(Long salary);

}
