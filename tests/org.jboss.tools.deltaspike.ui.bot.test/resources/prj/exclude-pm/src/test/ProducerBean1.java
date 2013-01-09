package test;

import javax.enterprise.inject.Produces;

public class ProducerBean1 {

	
	@Produces
	public int producer() {
		return 0;
	}
	
}
