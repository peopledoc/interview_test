package com.peopledoc.interview.api;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.peopledoc.interview.dto.RestaurantDTO;
import com.peopledoc.interview.service.RestaurantService;

@Api(name = "Restaurant", description = "Methods for managing restaurants")
@RestController
@RequestMapping("restaurants")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;

	/**
	 * Add a new restaurant
	 *
	 * @param restaurant
	 *            the restaurant to create
	 * @return The created restaurant
	 * @since 1.0
	 */
	@ApiMethod(description = "Add a new restaurant", responsestatuscode = "201 - Created")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<RestaurantDTO> add(@ApiPathParam(name = "restaurant", description = "The restaurant to create") @RequestBody RestaurantDTO restaurant) {

		try {
			restaurant = restaurantService.create(restaurant.getRestaurant());
		} catch (EntityExistsException e) {
			return new ResponseEntity<RestaurantDTO>(restaurant, HttpStatus.CONFLICT);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<RestaurantDTO>(restaurant, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<RestaurantDTO>(restaurant, HttpStatus.CREATED);
	}

	/**
	 * Get the list of all stored restaurants
	 *
	 * @return The list of all stored restaurants
	 * @since 1.0
	 */
	@ApiMethod(path = "/restaurants/", description = "Get the list of all stored restaurants")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	Iterable<RestaurantDTO> all() {
		List<RestaurantDTO> restaurants = restaurantService.findAll();
		return restaurants;
	}

	/**
	 * Delete an existing restaurant
	 *
	 * @param name
	 *            The name of the restaurant to delete
	 * @return The deleted restaurant
	 * @since 1.0
	 */
	@ApiMethod(description = "Delete an existing restaurant")
	@RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
	@ResponseBody
	ResponseEntity<RestaurantDTO> delete(@ApiPathParam(name = "name", description = "The name of the restaurant to delete") @PathVariable String name) {

		RestaurantDTO restaurant = new RestaurantDTO(name);
		try {
			restaurant = restaurantService.delete(name);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<RestaurantDTO>(restaurant, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<RestaurantDTO>(restaurant, HttpStatus.OK);
	}

	/**
	 * Get a random restaurant
	 *
	 * @return The random restaurant
	 * @since 1.0
	 */
	@ApiMethod(description = "Get a random restaurant")
	@RequestMapping(value = "/random", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<RestaurantDTO> random() {

		RestaurantDTO restaurant = restaurantService.findRandom();
		if (restaurant == null)
			return new ResponseEntity<RestaurantDTO>(restaurant, HttpStatus.NOT_FOUND);

		return new ResponseEntity<RestaurantDTO>(restaurant, HttpStatus.OK);
	}

}
