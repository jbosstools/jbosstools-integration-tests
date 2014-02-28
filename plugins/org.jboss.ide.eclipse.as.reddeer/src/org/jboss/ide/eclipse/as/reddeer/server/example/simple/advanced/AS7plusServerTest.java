package org.jboss.ide.eclipse.as.reddeer.server.example.simple.advanced;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqOperator;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS, version="7", operator=ServerReqOperator.GREATER_OR_EQUAL)
public class AS7plusServerTest {
	
	@Test
	public void testAS7(){
		System.out.println("Testing with present AS (version 7 or higher)!");
	}

}
