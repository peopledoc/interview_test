package com.peopledoc.interview.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.peopledoc.interview.Application;
import com.peopledoc.interview.dto.RestaurantDTO;
import com.peopledoc.interview.entity.Restaurant;
import com.peopledoc.interview.repository.RestaurantRepository;
import com.peopledoc.interview.service.RestaurantService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class RestaurantTest {

	@Autowired
	RestaurantRepository restaurantRepository;

	@Autowired
	RestaurantService restaurantService;

	/**
	 * Add manually restaurant into the database
	 *
	 * @param names
	 *            The list of names to add
	 */
	private void add(String... names) {
		for (String name : names)
			restaurantRepository.save(new Restaurant(name));
	}

	/**
	 * Retrieve manually restaurants from the database and assert the content
	 *
	 * @param names
	 *            The lit of restaurant names to check
	 */
	private void assertDatabase(String... names) {
		Set<String> restaurants = convert(restaurantRepository.findAll());
		assertThat(restaurants, hasSize(names.length));
		assertThat(restaurants, hasItems(names));
	}

	/**
	 * Clean up the database before each test
	 */
	@Before
	public void before() {
		restaurantRepository.deleteAll();
	}

	/**
	 * Convert the JPA list of restaurant into a Set of String in order to make
	 * checks easier
	 *
	 * @param restaurants
	 *            The JPA list of restaurant to convert
	 * @return The Set of String representation
	 */
	private Set<String> convert(Iterable<Restaurant> restaurants) {

		if (restaurants == null)
			return null;

		Set<String> result = new HashSet<String>();
		for (Restaurant restaurant : restaurants)
			result.add(restaurant.getName());

		return result;
	}

	/**
	 * Convert the DTO list of restaurant into a Set of String in order to make
	 * checks easier
	 *
	 * @param restaurants
	 *            The DTO list of restaurant to convert
	 * @return The Set of String representation
	 */
	private Set<String> convert(List<RestaurantDTO> restaurants) {

		if (restaurants == null)
			return null;

		Set<String> result = new HashSet<String>();
		for (RestaurantDTO restaurant : restaurants)
			result.add(restaurant.getRestaurant());

		return result;
	}

	@Test
	public void create() {
		restaurantService.create("test");
		assertDatabase("test");

	}

	@Test(expected = ConstraintViolationException.class)
	public void createEmpty() {
		restaurantService.create("");
	}

	@Test(expected = EntityExistsException.class)
	public void createExisting() {
		add("existing");
		restaurantService.create("existing");
	}

	@Test(expected = ConstraintViolationException.class)
	public void createInvalid() {
		restaurantService.create("t");
	}

	@Test(expected = ConstraintViolationException.class)
	public void createNull() {
		restaurantService.create(null);
	}

	@Test
	public void delete() {
		add("test");
		restaurantService.delete("test");
		assertDatabase();
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteEmpty() {
		add("test");
		restaurantService.delete("");
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteNull() {
		add("test");
		restaurantService.delete(null);
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteUnknown() {
		add("test");
		restaurantService.delete("unknown");
	}

	@Test
	public void find() {
		add("test");
		RestaurantDTO restaurant = restaurantService.find("test");
		assertThat(restaurant, not(nullValue()));
		assertThat(restaurant.getRestaurant(), is(equalTo("test")));
	}

	@Test
	public void findAll() {
		add("restaurant1", "restaurant2", "restaurant3");
		List<RestaurantDTO> restaurants = restaurantService.findAll();
		Set<String> names = convert(restaurants);
		assertThat(names, hasSize(3));
		assertThat(names, hasItems("restaurant1", "restaurant2", "restaurant3"));
	}

	@Test
	public void findAllEmpty() {
		List<RestaurantDTO> restaurants = restaurantService.findAll();
		Set<String> names = convert(restaurants);
		assertThat(names, hasSize(0));
	}

	@Test
	public void findEmpty() {
		add("test");
		RestaurantDTO restaurant = restaurantService.find("");
		assertThat(restaurant, nullValue());
	}

	@Test
	public void findNull() {
		add("test");
		RestaurantDTO restaurant = restaurantService.find(null);
		assertThat(restaurant, nullValue());
	}

	@Test
	public void findRandom() {
		add("restaurant1", "restaurant2", "restaurant3");
		Set<String> names = new HashSet<String>();
		for (int i = 0; i < 100; i++) {
			RestaurantDTO restaurant = restaurantService.findRandom();
			assertThat(restaurant, not(nullValue()));
			names.add(restaurant.getRestaurant());
		}
		assertThat(names, hasSize(3));
		assertThat(names, hasItems("restaurant1", "restaurant2", "restaurant3"));
	}

	@Test
	public void findRandomEmpty() {
		RestaurantDTO restaurant = restaurantService.find("unknown");
		assertThat(restaurant, nullValue());
	}

	@Test
	public void findRandomOne() {
		add("restaurant1");
		Set<String> names = new HashSet<String>();
		for (int i = 0; i < 100; i++) {
			RestaurantDTO restaurant = restaurantService.findRandom();
			assertThat(restaurant, not(nullValue()));
			names.add(restaurant.getRestaurant());
		}
		assertThat(names, hasSize(1));
		assertThat(names, hasItems("restaurant1"));
	}

	@Test
	public void findUnknown() {
		add("test");
		RestaurantDTO restaurant = restaurantService.find("unknown");
		assertThat(restaurant, nullValue());
	}

}
