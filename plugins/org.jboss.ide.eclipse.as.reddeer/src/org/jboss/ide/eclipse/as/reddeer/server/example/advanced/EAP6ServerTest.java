package org.jboss.ide.eclipse.as.reddeer.server.example.advanced;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.EAP6_0)
public class EAP6ServerTest {
	
	@Test
	public void testEAP6(){
		System.out.println("Testing with running EAP 6!");
	}

}
