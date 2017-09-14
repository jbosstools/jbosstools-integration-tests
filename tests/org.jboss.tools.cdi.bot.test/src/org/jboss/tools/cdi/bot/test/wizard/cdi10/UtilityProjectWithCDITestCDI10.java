package org.jboss.tools.cdi.bot.test.wizard.cdi10;

import java.util.Arrays;

import org.eclipse.reddeer.eclipse.jst.j2ee.ui.project.facet.UtilityProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.j2ee.ui.project.facet.UtilityProjectWizard;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.wizard.template.ProjectWithCDITemplate;
import org.jboss.tools.cdi.reddeer.matcher.ServerMatcher;
import org.junit.Before;

@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class UtilityProjectWithCDITestCDI10 extends ProjectWithCDITemplate{

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.AS());
	}
	
	public UtilityProjectWithCDITestCDI10(){
		enabledByDefault = true;
		PROJECT_NAME = "UtilityProject";
		expectedProblemAdded = Arrays.asList("Missing beans.xml file in the project");
		expectedProblem = "Missing beans.xml file in the project";
	}
	
	@Before
	public void createUtilityProject(){
		UtilityProjectWizard uw = new UtilityProjectWizard();
		uw.open();
		UtilityProjectFirstPage up = new UtilityProjectFirstPage(uw);
		up.setProjectName(PROJECT_NAME);
		uw.finish();
	}

}
