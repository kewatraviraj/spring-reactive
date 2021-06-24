package com.example.springreactivedemo.model;

import java.io.Serializable;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey(value = "id")
	private Integer id;

	@Column(value = "name")
	private String name;

	@Column(value = "phone")
	private String phone;

	@Column(value = "salary")
	private Long salary;

	public Integer getId() {
		return id;
	}

	public User() {
		super();
	}

	public User(Integer id, String name, String phone, Long salary) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.salary = salary;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getSalary() {
		return salary;
	}

	public void setSalary(Long salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", phone=" + phone + ", salary=" + salary + "]";
	}

}
