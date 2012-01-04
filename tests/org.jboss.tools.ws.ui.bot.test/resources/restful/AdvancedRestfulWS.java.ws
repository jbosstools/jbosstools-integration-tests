package {0};

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("/rest")
public class {1} '{'

	@GET
	@Path("/'{'id'}'")
	@Produces("text/plain")
	public String getMessage(@PathParam("id") int id) '{'
		return null;
	'}'
	
	@DELETE
	@Path("/delete/'{'id'}'")
	public void deleteMesage(@PathParam("id") int id) '{'
		//do nothing
	'}'
	
	@PUT
	@Path("/put/'{'id'}'")
	@Consumes("text/plain")
	public void addMessage(@PathParam("id") int id) '{'
		//do nothing
	'}'
	
	@POST
	@Path("/post/'{'id'}'")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String editMessage(@PathParam("id") int id) '{'
		return null;
	'}'
'}'
