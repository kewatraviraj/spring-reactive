package com.example.springreactivedemo.dto;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

public class UserDTO {

	@PrimaryKey(value = "id")
	private Integer id;

	@Column(value = "name")
	private String name;

	public UserDTO() {
		super();
	}

	public UserDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
