package org.jboss.tools.central.test.ui.reddeer.examples.tests;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.central.reddeer.projects.CentralExampleProject;
import org.junit.Test;

@JBossServer(type = ServerReqType.ANY, state = ServerReqState.RUNNING)
public class BackEndApplicationsTest extends AbstractExamplesTest{

	@Test
	public void helloworldRsTest() {
		importAndDeployExample(new CentralExampleProject("helloworld-rs",
				"jboss-helloworld-rs", "Back-end Applications"));
	}
	
	
	@Test
	public void helloworldJmsTest(){
		importExample(new CentralExampleProject("helloworld-jms", "jboss-helloworld-jms", "Back-end Applications"));
	}
	
	@Test
	public void helloworldMdbTest(){
		importAndDeployExample(new CentralExampleProject("helloworld-mdb", "jboss-helloworld-mdb", "Back-end Applications"));
	}
	
	@Test
	public void deltaspikeHelloworldJmsTest(){
		importAndDeployExample(new CentralExampleProject("deltaspike-helloworld-jms", "deltaspike-helloworld-jms", "Back-end Applications"));
	}
	
}
