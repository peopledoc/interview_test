package com.peopledoc.interview.dto;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(name = "Restaurant")
public class RestaurantDTO {

	@ApiObjectField(description = "The name of the restaurant", required = true)
	private String restaurant;

	public RestaurantDTO() {
	}

	public RestaurantDTO(String restaurant) {
		this.restaurant = restaurant;
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

}
