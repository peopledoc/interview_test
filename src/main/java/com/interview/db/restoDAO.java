package com.interview.db;

import com.google.common.base.Optional;
import com.interview.core.resto;

import io.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;

import java.util.List;

public class restoDAO extends AbstractDAO<resto> {
    public restoDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<resto> findById(int id) {
        return Optional.fromNullable(get(id));
    }
    
    public resto findByName(String name) {
        return get(name);
    }

    public resto create(resto restaurant) {
        return persist(restaurant);
    }
    
    public void delete(int id){
        // currentSession().delete(id);
    	currentSession().getNamedQuery("com.test.resto.delete").setInteger("id", id);
    }

    public List<resto> findAll() {
        return list(namedQuery("com.test.resto.findAll"));
    }
}