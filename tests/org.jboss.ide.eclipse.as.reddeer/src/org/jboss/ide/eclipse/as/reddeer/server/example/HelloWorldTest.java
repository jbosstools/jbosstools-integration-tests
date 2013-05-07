package org.jboss.ide.eclipse.as.reddeer.server.example;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@Server
public class HelloWorldTest {
	
	@Test
	public void testHelloWorld(){
		System.out.println("Hello World!");
	}

}
