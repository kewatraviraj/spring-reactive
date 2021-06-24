package com.example.springreactivedemo.model;

import java.util.Map;

public class APIResponse {

	public APIResponse(int status, boolean success, String message, Object data, Map<String, Object> meta) {
		super();
		this.status = status;
		this.success = success;
		this.message = message;
		this.data = data;
		this.meta = meta;
	}

	public APIResponse(int status, boolean success, String message, Object data) {
		super();
		this.status = status;
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public APIResponse(int status, boolean success, String message) {
		super();
		this.status = status;
		this.success = success;
		this.message = message;
	}

	public APIResponse() {
		super();
	}

	// HttpStatus response code
	private int status;

	// success
	private boolean success;

	// General error message about nature of error
	private String message;

	// data to send in response
	private Object data;

	// meta data to send in response
	private Map<String, Object> meta;

	// Getter and setters
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}
}
