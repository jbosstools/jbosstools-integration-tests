package org.jboss.tools.cdi.bot.test.wizard.cdi10;

import java.util.Arrays;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jst.j2ee.ui.project.facet.UtilityProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.j2ee.ui.project.facet.UtilityProjectWizard;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.wizard.template.ProjectWithCDITemplate;
import org.junit.Before;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class UtilityProjectWithCDITestCDI10 extends ProjectWithCDITemplate{
	
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
		UtilityProjectFirstPage up = new UtilityProjectFirstPage();
		up.setProjectName(PROJECT_NAME);
		uw.finish();
	}

}
