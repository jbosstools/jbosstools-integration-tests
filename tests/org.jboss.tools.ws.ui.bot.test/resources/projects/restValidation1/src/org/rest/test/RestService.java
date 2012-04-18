package org.rest.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path("/rest")
public class RestService {

	@GET
	@Path("/{id}")
	public String getMessage(@PathParam("id") int id) {
		return null;
	}
	
}