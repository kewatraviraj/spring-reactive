package com.example.springreactivedemo.controller;

import org.modelmapper.ModelMapper;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.springreactivedemo.dto.UserDTO;
import com.example.springreactivedemo.model.User;
import com.example.springreactivedemo.service.ReactiveDemoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveDemoController {

	@Autowired
	private ReactiveDemoService reactiveDemoService;

	@Autowired
	private WebClient webClient;

//	@PostConstruct
//	public void saveUsers() {
//		List<User> usersList = new ArrayList<User>();
//		usersList.add(new User(1, "John", "9854752145", 2000L));
//		usersList.add(new User(2, "Mike", "9532145785", 3000L));
//		usersList.add(new User(3, "Adam", "9632147859", 5000L));
//		usersList.add(new User(4, "Kevin", "9452154785", 7000L));
//		usersList.add(new User(5, "Robert", "9235478452", 9000L));
//		usersList.add(new User(6, "Drake", "9325874512", 10000L));
//		reactiveDemoService.initializeUsers(usersList);
//	}

	@RequestMapping(value = { "/add-bulk-users" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public void addUsers() {
		for (int i = 1; i < 100; i++) {
			User user = new User();
			user.setId(i);
			user.setName("user_" + i);
			user.setPhone("" + (long) Math.floor(Math.random() * 9000000000L) + 10L);
			user.setSalary(1000L + i);
			reactiveDemoService.createUser(user);
		}
	}

	@PostMapping(value = "add-user")
	public void createUser(@RequestBody User user) {
		reactiveDemoService.createUser(user);
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<User>> updateUser(@PathVariable Integer id, @RequestBody User user) {
		return reactiveDemoService.updateUser(id, user).map(updatedUser -> ResponseEntity.ok(updatedUser))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<User> findAll() {
		return reactiveDemoService.getAllUsers();
	}

	@GetMapping(value = "get-users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<ResponseEntity> getAllUsers() {
		ModelMapper modelMapper = new ModelMapper();
		return reactiveDemoService.fetchUsers().map(user -> modelMapper.map(user, UserDTO.class)).collectList()
				.map(user -> ResponseEntity.ok(user));
	}

	@GetMapping(value = "/user/{id}")
	public Mono<ResponseEntity<UserDTO>> getUser(@PathVariable("id") Integer id) {
		return reactiveDemoService.findById(id)
				.map(user -> ResponseEntity.ok(new UserDTO(user.getId(), user.getName())))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/get-user/{id}")
	public Mono<User> findById(@PathVariable("id") Integer id) {
		User user = new User();
		Mono<User> response = reactiveDemoService.findById(id).defaultIfEmpty(user).flatMap(result -> {
			System.out.println(result);
			if (result.getId() != null) {
				return Mono.just(result);
			} else {
				return Mono.error(new NotFound());
			}
		}).onErrorReturn(new User());
//		HttpStatus status = user != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return response;
	}

	@GetMapping(value = "/name/{name}")
	public Flux<User> findByName(@PathVariable("name") String name) {
		return reactiveDemoService.findByName(name);
	}

	@GetMapping(value = "/salary/{name}")
	public Flux<User> findBySalary(@PathVariable("name") Long salary) {
		return reactiveDemoService.findBySalary(salary).map(user -> {
			if (null == user.getId()) {
				throw new RuntimeException("ERROR");
			}
			return user;
		}).onErrorResume(ReactiveDemoController::fallbackMethod);
	}

	private static Mono<User> fallbackMethod(Throwable error) {
		if (error instanceof RuntimeException) {
			return Mono.just(new User(null, "RUNTIME_EX", null, null));
		} else {
			return Mono.just(new User(null, "NOT_RUNTIME_EX", null, null));
		}
	}

	@GetMapping(value = "/fetch-users")
	public Flux<User> fetchUsers() {

		WebClient webClient = WebClient.create("https://jsonplaceholder.typicode.com");

		Flux<User> users = webClient.get().uri("/users")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve().bodyToFlux(User.class);
		System.out.println(users);

		return users;
	}

	@GetMapping(value = "/retrieve-users")
	public Flux<User> retrieveUsers() {

		Flux<User> users = webClient.get().uri("/users")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
				.onStatus(HttpStatus::is4xxClientError, response -> {
					return Mono.error(new RuntimeException("4xx"));
				}).onStatus(HttpStatus::is5xxServerError, response -> {
					return Mono.error(new RuntimeException("5xx"));
				}).bodyToFlux(User.class);
		
		System.out.println(users);
		return users;
	}
}
