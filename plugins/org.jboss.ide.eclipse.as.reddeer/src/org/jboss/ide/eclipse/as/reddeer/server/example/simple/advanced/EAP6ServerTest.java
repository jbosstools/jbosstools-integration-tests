package org.jboss.ide.eclipse.as.reddeer.server.example.simple.advanced;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@Server(state=ServerReqState.RUNNING, type=ServerReqType.EAP, version="6")
public class EAP6ServerTest {
	
	@Test
	public void testEAP6(){
		System.out.println("Testing with running EAP 6!");
	}

}
