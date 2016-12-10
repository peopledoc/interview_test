package com.peopledoc.interview.test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;

import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.peopledoc.interview.Application;
import com.peopledoc.interview.entity.Restaurant;
import com.peopledoc.interview.repository.RestaurantRepository;
import com.peopledoc.interview.service.RestaurantService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class RestaurantIT {

	@Autowired
	RestaurantRepository restaurantRepository;

	@Autowired
	RestaurantService restaurantService;

	@Value("${local.server.port}")
	private int serverPort;

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
		RestAssured.port = serverPort;
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

	@Test
	public void testCreate() {
		String response = given().contentType(ContentType.JSON).body("{\"restaurant\":\"new\"}") //
				.when().post("/restaurants").then() //
				.statusCode(HttpStatus.SC_CREATED) //
				.contentType(ContentType.JSON) //
				.extract().response().asString();
		assertThat(response, is("{\"restaurant\":\"new\"}"));
		assertDatabase("new");
	}

	@Test
	public void testCreateEmpty() {
		given().contentType(ContentType.JSON).body("{\"restaurant\":\"\"}") //
				.when().post("/restaurants").then() //
				.statusCode(HttpStatus.SC_BAD_REQUEST);
		assertDatabase();
	}

	@Test
	public void testCreateExisting() {
		add("existing");
		given().contentType(ContentType.JSON).body("{\"restaurant\":\"existing\"}") //
				.when().post("/restaurants").then() //
				.statusCode(HttpStatus.SC_CONFLICT);
		assertDatabase("existing");
	}

	@Test
	public void testCreateInvalid() {
		given().contentType(ContentType.JSON).body("{\"restaurant\":\"t\"}") //
				.when().post("/restaurants").then() //
				.statusCode(HttpStatus.SC_BAD_REQUEST);
		assertDatabase();
	}

	@Test
	public void testCreateNull() {
		given().contentType(ContentType.JSON).body("{}") //
				.when().post("/restaurants").then() //
				.statusCode(HttpStatus.SC_BAD_REQUEST);
		assertDatabase();
	}

	@Test
	public void testDelete() {
		add("test1", "test2", "test3");
		String response = given().contentType(ContentType.JSON) //
				.when().delete("/restaurants/test2").then() //
				.statusCode(HttpStatus.SC_OK) //
				.contentType(ContentType.JSON) //
				.extract().response().asString();
		assertDatabase("test1", "test3");
		assertThat(response, is("{\"restaurant\":\"test2\"}"));
	}

	@Test
	public void testDeleteUnknown() {
		add("test1", "test2", "test3");
		String response = given().contentType(ContentType.JSON) //
				.when().delete("/restaurants/unknown").then() //
				.statusCode(HttpStatus.SC_NOT_FOUND) //
				.contentType(ContentType.JSON) //
				.extract().response().asString();
		assertDatabase("test1", "test2", "test3");
		assertThat(response, is("{\"restaurant\":\"unknown\"}"));
	}

	@Test
	public void testFindAll() {
		add("test1", "test2", "test3");
		String response = given().contentType(ContentType.JSON) //
				.when().get("/restaurants").then() //
				.statusCode(HttpStatus.SC_OK) //
				.contentType(ContentType.JSON) //
				.extract().response().asString();
		assertThat(response, is("[{\"restaurant\":\"test1\"},{\"restaurant\":\"test2\"},{\"restaurant\":\"test3\"}]"));
	}

	@Test
	public void testFindAllEmpty() {
		String response = given().contentType(ContentType.JSON) //
				.when().get("/restaurants").then() //
				.statusCode(HttpStatus.SC_OK) //
				.contentType(ContentType.JSON) //
				.extract().response().asString();
		assertThat(response, is("[]"));
	}

	@Test
	public void testRandom() {
		add("random1", "random2", "random3");
		Set<String> results = new HashSet<String>();
		for (int i = 0; i < 10; i++) {
			String response = given().contentType(ContentType.JSON) //
					.when().get("/restaurants/random").then() //
					.statusCode(HttpStatus.SC_OK) //
					.contentType(ContentType.JSON) //
					.extract().response().asString();
			results.add(response);
			assertThat(response, isOneOf("{\"restaurant\":\"random1\"}", "{\"restaurant\":\"random2\"}", "{\"restaurant\":\"random3\"}"));
		}
		assertThat(results, hasSize(3));
	}

	@Test
	public void testRandomEmpty() {
		given().contentType(ContentType.JSON) //
				.when().get("/restaurants/random").then() //
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}
}
