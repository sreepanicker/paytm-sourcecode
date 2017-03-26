package com.sree.paytmhelloapp;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author sreejithu.panicker
 * this Class generates Helloworld  greeting to the user.
 */

@Path("/helloworld")
public class HelloWorld {
    
    @GET
    @Path("greeting")
    @Produces(MediaType.TEXT_PLAIN)
    public String display(){
        return "Hello world";
    }
    
}
