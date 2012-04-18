package org.rest.test;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;


@Path("/rest")
public class RestService {

	@GET
	public String getMessage() {
		return null;
	}
	
	@DELETE
	public void deleteMesage() {
		//do nothing
	}
	
	@PUT	
	public void addMessage() {
		//do nothing
	}
	
	@POST	
	public String editMessage() {
		return null;
	}
}
