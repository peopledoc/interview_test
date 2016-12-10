package com.peopledoc.interview.repository;

import org.springframework.data.repository.CrudRepository;

import com.peopledoc.interview.entity.Restaurant;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

	Restaurant findByName(String name);

}