package com.interview.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

public class restaurants {
    private int id;

    @Length(max = 3)
    private String name;

    public restaurants() {
    }

    public restaurants(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
}