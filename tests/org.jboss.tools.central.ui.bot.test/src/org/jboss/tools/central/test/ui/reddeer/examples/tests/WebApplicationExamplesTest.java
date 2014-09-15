package org.jboss.tools.central.test.ui.reddeer.examples.tests;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.central.reddeer.projects.CentralExampleProject;
import org.junit.Test;

@JBossServer(type = ServerReqType.ANY, state = ServerReqState.RUNNING)
public class WebApplicationExamplesTest extends AbstractExamplesTest{

	@Test
	public void kitchensinkTest() {
		importAndDeployExample(new CentralExampleProject("kitchensink",
				"jboss-kitchensink", "Web Applications"));
	}

	@Test
	public void greeterTest() {
		importAndDeployExample(new CentralExampleProject("greeter",
				"jboss-greeter", "Web Applications"));
	}

	@Test
	public void helloworldTest() {
		importAndDeployExample(new CentralExampleProject("helloworld",
				"jboss-helloworld", "Web Applications"));
	}

	@Test
	public void kitchensinkRfTest() {
		importAndDeployExample(new CentralExampleProject("kitchensink-rf",
				"jboss-kitchensink-rf", "Web Applications"));
	}
	
	@Test
	public void kitchensinkAngularJSTest(){
		importAndDeployExample(new CentralExampleProject("kitchensink-angularjs", "jboss-kitchensink-angularjs", "Web Applications"));
	}
	
}
