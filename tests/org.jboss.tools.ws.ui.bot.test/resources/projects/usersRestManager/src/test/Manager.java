package test;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class Manager {

	private List<User> users = null;
	
	public List<User> getAllUsers() {
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
