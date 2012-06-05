package org.rest.test;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;

@Path("/rest")
public class RestService {

	@GET
	public void getBooks(@MatrixParam("author") java.lang.String param1,
						 @MatrixParam("country") java.lang.Integer param2) {
		
	}
	
}