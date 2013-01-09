package test;

import javax.enterprise.inject.Produces;

public class ProducerBean2 {

	
	@Produces
	public int producer() {
		return 0;
	}
	
}
