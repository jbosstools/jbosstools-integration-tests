package org.jboss.tools.central.test.ui.reddeer.examples.tests;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.central.reddeer.projects.CentralExampleProject;
import org.junit.Test;

@JBossServer(type = ServerReqType.ANY, state = ServerReqState.RUNNING)
public class PortalApplicationsTest extends AbstractExamplesTest{

	@Test
	public void simplestHelloWorldPortlet(){
		importExample(new CentralExampleProject("helloworld-rs", "simplest-hello-world-portlet", "Back-end Applications"));
	}
	
}
