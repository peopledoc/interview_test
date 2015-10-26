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

    private final HibernateBundle<testConfiguration> hibernate = new HibernateBundle<testConfiguration>(resto.class) {
        public DataSourceFactory getDataSourceFactory(testConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<testConfiguration> bootstrap) {
    	bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(testConfiguration configuration,
                    Environment environment) {
    	
    	
    	final restoDAO dao = new restoDAO(hibernate.getSessionFactory());
    	final testResource resource = new testResource(dao);
/*    	final testHealthCheck healthCheck =
    	        new testHealthCheck(configuration.getTemplate());
    	environment.healthChecks().register("template", healthCheck);*/
    	
    	environment.jersey().register(resource);
    }

}