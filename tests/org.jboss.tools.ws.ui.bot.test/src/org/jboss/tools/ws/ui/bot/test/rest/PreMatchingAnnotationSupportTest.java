package org.jboss.tools.ws.ui.bot.test.rest;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

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
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerRequirementState.PRESENT)
@AutoBuilding(value = false, cleanup = true)
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
		importWSTestProject(projectName);
		ProjectHelper.cleanAllProjects();
		
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 1);
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName,
				"@PreMatching annotation is only allowed on subclasses of javax.ws.rs.container.ContainerRequestFilter", null, 1);
	}
}
