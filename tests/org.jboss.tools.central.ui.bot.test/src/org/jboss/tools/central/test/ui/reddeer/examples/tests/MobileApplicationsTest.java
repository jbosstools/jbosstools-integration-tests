package org.jboss.tools.central.test.ui.reddeer.examples.tests;

import static org.junit.Assert.fail;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.central.reddeer.projects.CentralExampleProject;
import org.junit.Test;

@JBossServer(type = ServerReqType.EAP, state = ServerReqState.RUNNING)
public class MobileApplicationsTest extends AbstractExamplesTest{
	
	@Test
	public void kitchensinkHTML5MobileTest() {
		importAndDeployExample(new CentralExampleProject(
				"kitchensink-html5-mobile",
				"jboss-kitchensink-html5-mobile", "Mobile Applications"));
	}

	@Test
	public void kitchensinkBackboneTest() {
		importAndDeployExample(new CentralExampleProject(
				"kitchensink-backbone", "jboss-kitchensink-backbone",
				"Mobile Applications"));
	}
	
	@Test
	public void contactsMobileBasicTest(){
		try{
			importAndDeployExample(new CentralExampleProject("contacts-mobile-basic", "jboss-contacts-mobile-basic", "Mobile Applications"));
		}catch(AssertionError err){
			if (!err.getMessage().contains("requires JBoss Hybrid Mobile Application")){
				fail(err.getMessage());
			}
		}
	}

}
