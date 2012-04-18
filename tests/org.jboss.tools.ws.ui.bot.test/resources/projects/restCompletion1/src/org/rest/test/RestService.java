package org.rest.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path("/rest")
public class RestService {

	@GET
	@Path("/{userId}")
	public String getMessage(@PathParam("") int id) {
		return null;
	}
	
}
