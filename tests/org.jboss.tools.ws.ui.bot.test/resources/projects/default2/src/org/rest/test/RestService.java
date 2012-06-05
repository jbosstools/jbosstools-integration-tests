package org.rest.test;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;

@Path("/rest")
public class RestService {

	@GET
	public void method(@MatrixParam("param") @DefaultValue("abc") java.lang.String parameter) {
		
	}
	
}