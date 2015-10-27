package com.interview.resources;

import com.codahale.metrics.annotation.Timed;
import com.interview.core.Resto;
import com.interview.db.RestoDAO;

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

/* 
 * In this Jersey resource all the REST methods for the class
 * URI (in this case /restaurants) are implemented.
 * Each method can have an optional path which will be 
 * relative to the class URI. 
 */

public class TestResource {
    private Random randomIndex;
    private final RestoDAO restodao;
    
    public TestResource(RestoDAO restodao) {
        this.restodao = restodao;
    }
    
    /* 
     * The POST method is the one in charge of the creation
     * and persistence of new data. Data is persisted by the
     * function create, declared in restoDAO. Each new 
     * restaurant has to be passed as JSON elements. For
     * example:
     * {"id":1,"name":"mc'donalds"} 
     */
    
    @POST
    @Timed
    @UnitOfWork(transactional = false)    
    public Resto createResto(Resto restaurant) {
        return restodao.create(restaurant);
    }
    
    /*
     * The DELETE method removes persisted data. Since each
     * restaurant in the list has a unique id (which is also the
     * primary key of the db), the deletion is done by id instead
     * of name. 
     */
    
    @DELETE
    @Path("/{id}")
    @UnitOfWork(transactional = false)
    public String deleteResto(@PathParam("id") int id) {
    	try {
    		restodao.delete(id);
    		return "Message code: 200"; // Just an example message code.
    	} catch (Exception e) {
    		return e.toString();
    	}
    }
    
    /* 
     * In this first GET method, a sub path is used to generate and 
     * retrieve only one random restaurant from the list. The result
     * is retrieved as JSON containing the id and name of the 
     * restaurant.
     */

    @GET
    @Path("/random")
    @UnitOfWork(transactional = false)
    @Timed   
    public Resto randomResto() {
    	List<Resto> list = restodao.findAll();
        randomIndex = new Random();
        int index = randomIndex.nextInt(list.size());
        return list.get(index);
    }
    
    /*
     * The second GET method retrieves the full list of restaurants
     * in JSON format from the absolute URI path of the 
     * class (/restaurants).
     */
    
    @GET
    @Timed
    @UnitOfWork(transactional = false)
    public List<Resto> getListResto() {
    	return restodao.findAll();
    }    
}
