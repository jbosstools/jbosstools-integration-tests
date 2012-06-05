package org.rest.test;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/rest")
public class RestService {

	@GET
	@Path("/{id}")
	public void method(@PathParam("id") @DefaultValue("abc") java.lang.String parameter) {
		
	}
	
}