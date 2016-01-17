package com.peopledoc.interview.service;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peopledoc.interview.dto.RestaurantDTO;
import com.peopledoc.interview.entity.Restaurant;
import com.peopledoc.interview.repository.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository repository;

	/**
	 * Convert a set of JPA restaurants into a DTO list equivalent
	 *
	 * @param restaurants
	 *            The set of JPA restaurants to convert
	 * @return The DTO list equivalent
	 */
	private List<RestaurantDTO> convert(Iterable<Restaurant> restaurants) {

		if (restaurants == null)
			return null;

		List<RestaurantDTO> result = new LinkedList<RestaurantDTO>();
		for (Restaurant restaurant : restaurants)
			result.add(convert(restaurant));

		return result;
	}

	/**
	 * Convert a JPA restaurant into a DTO equivalent
	 *
	 * @param restaurant
	 *            The JPA restaurant to convert
	 * @return The DTO representation
	 */
	private RestaurantDTO convert(Restaurant restaurant) {

		if (restaurant == null)
			return null;

		RestaurantDTO result = new RestaurantDTO();
		result.setRestaurant(restaurant.getName());
		return result;
	}

	/**
	 * Create and store a new restaurant
	 *
	 * @param name
	 *            The name of the restaurant to create
	 * @return The DTO representation
	 */
	public synchronized RestaurantDTO create(String name) throws EntityExistsException {

		RestaurantDTO restaurantDto = find(name);
		if (restaurantDto != null)
			throw new EntityExistsException("This restaurant already exits");

		Restaurant restaurant = new Restaurant(name);
		restaurant = repository.save(restaurant);
		return convert(restaurant);
	}

	/**
	 * Delete an existing restaurant
	 *
	 * @param name
	 *            The name of the restaurant to delete
	 * @return The DTO representation
	 */
	public RestaurantDTO delete(String name) throws EntityNotFoundException {

		Restaurant restaurant = repository.findByName(name);
		if (restaurant == null)
			throw new EntityNotFoundException();

		repository.delete(restaurant);
		return convert(restaurant);
	}

	/**
	 * Find a restaurant
	 *
	 * @param name
	 *            The name of the restaurant to find
	 * @return The DTO representation
	 */
	public RestaurantDTO find(String name) {
		Restaurant restaurant = repository.findByName(name);
		return convert(restaurant);
	}

	/**
	 * Find all restaurants
	 *
	 * @return The list of DTO representations
	 */
	public List<RestaurantDTO> findAll() {
		Iterable<Restaurant> restaurants = repository.findAll();
		return convert(restaurants);
	}

	/**
	 * Find a random restaurant
	 *
	 * @return The DTO representation
	 */
	public RestaurantDTO findRandom() {

		List<RestaurantDTO> restaurants = findAll();
		if (restaurants == null || restaurants.isEmpty())
			return null;

		SecureRandom random = new SecureRandom();
		int index = random.nextInt(restaurants.size());
		return restaurants.get(index);
	}

	/**
	 * Get a restaurant
	 *
	 * @param name
	 *            The name of the restaurant to get
	 * @return The DTO representation
	 */
	public RestaurantDTO get(String name) throws EntityNotFoundException {

		Restaurant restaurant = repository.findByName(name);
		if (restaurant == null)
			throw new EntityNotFoundException();

		return convert(restaurant);
	}

}
