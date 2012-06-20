package test;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/")
public class RestService {

	@GET
	@Path("/get")
	public String getMethod() {
		return "GET method";
	}
	
	@POST
	@Path("/post")
	public String postMethod() {
		return "POST method";
	}
	
	@PUT
	@Path("/put")
	public String putMethod() {
		return "PUT method";
	}
	
	@DELETE
	@Path("/delete")
	public String deleteMethod() {
		return "DELETE method";
	}
	
}
