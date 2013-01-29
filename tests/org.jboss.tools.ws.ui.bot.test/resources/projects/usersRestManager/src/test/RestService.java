package test;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class RestService {

	private List<User> users = null;
	
	@GET
	@Path("/xml")
	@Produces(MediaType.APPLICATION_XML)
	public List<User> getAllUsers() {
		return generateUsers();
	}
	
	@GET
	@Path("/json")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers2() {
		return generateUsers();
	}
	
	private List<User> generateUsers() {
		if (users == null) {
			users = new LinkedList<User>();
			users.add(new User(1, "James", new BigInteger("6545646")));
			users.add(new User(2, "John", new BigInteger("8546544")));
			users.add(new User(3, "Paul", new BigInteger("1287475")));
		}
		return users;
	}
}
