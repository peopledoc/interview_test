package com.interview.core;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "listRestaurants")
@NamedQueries({
        @NamedQuery(
                name = "com.test.resto.findAll",
                query = "SELECT p FROM resto p"
        ),
        @NamedQuery(
                name = "com.test.resto.delete",
                query = "DELETE FROM resto s WHERE s.id = :id"
        )        
})
public class Resto {
    @Id
    @Column(name = "id", nullable = false)
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "name", nullable = false)
    private String name;

    public Resto() {
    }

    public Resto(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
   
}
