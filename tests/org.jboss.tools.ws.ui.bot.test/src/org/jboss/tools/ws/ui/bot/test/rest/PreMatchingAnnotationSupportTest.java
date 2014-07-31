package org.jboss.tools.ws.ui.bot.test.rest;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.junit.Test;

/**
 * Testing support for {@link javax.ws.rs.container.PreMatching}
 * 
 * Run with J2EE7+ server
 * 
 * @author Radoslav Rabara
 * 
 * @see http://tools.jboss.org/documentation/whatsnew/jbosstools/4.2.0.Beta1.html#webservices
 * @since JBT 4.2.0.Beta1
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY)
public class PreMatchingAnnotationSupportTest extends RESTfulTestBase {
	
	@Override
	public void setup() {
		// no setup required
	}
	
	@Test
	public void useOnContainerRequestFilterTest() {
		importAndCheckErrors("prematching1");
	}
	
	@Test
	public void useOnNotSupportedTypeTest() {
		String projectName = "prematching2";
		importRestWSProject(projectName);
		
		assertCountOfValidationErrors(projectName, 1);
		assertCountOfValidationErrors(projectName,
				"@PreMatching annotation is only allowed on subclasses of javax.ws.rs.container.ContainerRequestFilter", 1);
	}
}
