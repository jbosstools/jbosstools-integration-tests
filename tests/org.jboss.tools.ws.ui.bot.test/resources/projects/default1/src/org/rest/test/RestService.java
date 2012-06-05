package org.rest.test;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/rest")
public class RestService {

	@GET
	public void method(@QueryParam("param") @DefaultValue("abc") java.lang.String parameter) {
		
	}
	
}
