package org.jboss.ide.eclipse.as.reddeer.server.example.simple;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@JBossServer()
public class HelloWorldTest {
	
	@InjectRequirement
	protected ServerRequirement requirement;
	
	@Test
	public void testHelloWorld(){
		System.out.println("Hello World!");
	}

}
