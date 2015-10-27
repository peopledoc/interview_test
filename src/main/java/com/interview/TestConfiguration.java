package com.interview;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.db.DataSourceFactory;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TestConfiguration extends Configuration {
    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }
    
    /*
     * This next lines are needed for dropwizard to recognize
     * the database keyword in the YAML configuration file. 
     * It is in the YAML configuration file that all the 
     * JDBC parameters (host, username, database name, etc) 
     * are located.
     */
        
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
    
    public void setDatabase(DataSourceFactory database) {
        this.database = database;
    }
}
