package test;

import javax.enterprise.inject.Produces;
import javax.ws.rs.core.Application;

public class Producer {

	@Produces
	public Application getApplication() {
		return new Application();
	}
	
}
