package com.interview;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.interview.core.Resto;
import com.interview.db.RestoDAO;
import com.interview.resources.TestResource;

public class TestApplication extends Application<TestConfiguration> {
	public static void main(String[] args) throws Exception {
		new TestApplication().run(args);
	}

	// Create the hibernate object to manage the connection to the database via jdbc (parameters in the YAML config file - test.YML):
	private final HibernateBundle<TestConfiguration> hibernate = new HibernateBundle<TestConfiguration>(Resto.class) {
		public DataSourceFactory getDataSourceFactory(TestConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	// Initialization of dropwizard application (containing the hibernate object with parameters from testConfiguration):
	@Override
	public void initialize(Bootstrap<TestConfiguration> bootstrap) {
		bootstrap.addBundle(hibernate);
	}

	@Override
	public void run(TestConfiguration configuration,
			Environment environment) {

		// Initialize the data access object (dao):
		final RestoDAO dao = new RestoDAO(hibernate.getSessionFactory());
		// Initialize the resource including the newly created dao:
		final TestResource resource = new TestResource(dao);

		/*    	final testHealthCheck healthCheck =
    	        new testHealthCheck(configuration.getTemplate());
    	environment.healthChecks().register("template", healthCheck);*/

		// Register the resource to Jersey:
		environment.jersey().register(resource);
	}

}