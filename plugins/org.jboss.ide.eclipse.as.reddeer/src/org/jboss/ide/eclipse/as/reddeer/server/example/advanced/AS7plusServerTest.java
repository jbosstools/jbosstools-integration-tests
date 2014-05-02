package org.jboss.ide.eclipse.as.reddeer.server.example.advanced;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqVersion;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_0, version=ServerReqVersion.GREATER_OR_EQUAL)
public class AS7plusServerTest {
	
	@Test
	public void testAS7(){
		System.out.println("Testing with present AS (version 7 or higher)!");
	}

}
