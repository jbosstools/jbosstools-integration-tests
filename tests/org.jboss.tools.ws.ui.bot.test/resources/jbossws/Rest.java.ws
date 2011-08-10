package rest.sample;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/RESTSample")
public class Sample {

	@GET()	
	@Produces("text/plain")
	public String method1() {
	    return "Hello World!";
	}
	
	
	
	@GET()
	@Path("{name}")
	@Produces("text/plain")
	public String method2(@PathParam("name")String name) {
	    return "Hello " + name;
	}
	
	@POST()
	@Path("PostMethod")
	@Produces("text/plain")
	public void method3() {
	    
	}
	
	@PUT()
	@Path("PutMethod")	
	public void method4() {
		
	}
	
	
	@DELETE()
	@Path("DeleteMethod")	
	public void method5() {
		
	}
	
}
