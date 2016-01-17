package com.peopledoc.interview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Restaurant {

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	@NotNull
	@Size(min = 2, max = 1024)
	private String name;

	public Restaurant() {
	}

	public Restaurant(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}