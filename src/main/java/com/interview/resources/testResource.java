package com.interview.resources;

import com.codahale.metrics.annotation.Timed;
import com.interview.core.resto;
import com.interview.db.restoDAO;

import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;
// import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
public class testResource {
    // private final String template;
    // private final List<String> defaultName;
    // private final AtomicLong counter = new AtomicLong();
    private Random randomIndex;
    private final restoDAO restodao;
    
    public testResource(restoDAO restodao) {
        this.restodao = restodao;
    }
    
    @POST
    @Timed
    @UnitOfWork(transactional = false)    
    public resto createResto(resto restaurant) {
        return restodao.create(restaurant);
    }
    
    @DELETE
    @Path("/{id}")
    @UnitOfWork(transactional = false)
    public String deleteResto(@PathParam("id") int id) {
    	try {
    		restodao.delete(id);
    		return "Message code: 200"; // Juste un example d'un code d'erreur personalisé.
    	} catch (Exception e) {
    		return e.toString();
    	}
    }

    @GET
    @Path("/random")
    @UnitOfWork(transactional = false)
    @Timed   
    public resto randomResto() {
    	List<resto> list = restodao.findAll();
        randomIndex = new Random();
        int index = randomIndex.nextInt(list.size());
        return list.get(index);
    }
    
    @GET
    @Timed
    @UnitOfWork(transactional = false)
    public List<resto> getListResto() {
    	return restodao.findAll();
    }    
}
