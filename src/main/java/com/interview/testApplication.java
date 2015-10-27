package com.interview;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.interview.core.resto;
import com.interview.db.restoDAO;
import com.interview.resources.testResource;

public class testApplication extends Application<testConfiguration> {
    public static void main(String[] args) throws Exception {
        new testApplication().run(args);
    }
    
    // Create the hibernate object to manage the connection to the database via jdbc (parameters in the YAML config file - test.YML):
    private final HibernateBundle<testConfiguration> hibernate = new HibernateBundle<testConfiguration>(resto.class) {
        public DataSourceFactory getDataSourceFactory(testConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };
    
    // Initialization of dropwizard application (containing the hibernate object with parameters from testConfiguration):
    @Override
    public void initialize(Bootstrap<testConfiguration> bootstrap) {
    	bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(testConfiguration configuration,
                    Environment environment) {
    	
    	// Initialize the data access object (dao):
    	final restoDAO dao = new restoDAO(hibernate.getSessionFactory());
    	// Initialize the resource including the newly created dao:
    	final testResource resource = new testResource(dao);
    	
/*    	final testHealthCheck healthCheck =
    	        new testHealthCheck(configuration.getTemplate());
    	environment.healthChecks().register("template", healthCheck);*/
    	
    	// Register the resource to Jersey:
    	environment.jersey().register(resource);
    }

}