package org.jboss.tools.cdi.bot.test.wizard.cdi10;

import org.eclipse.reddeer.eclipse.jst.ejb.ui.project.facet.EjbProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.ejb.ui.project.facet.EjbProjectWizard;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.wizard.template.ProjectWithCDITemplate;
import org.junit.Before;

@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class EjbProjectWithCDITestCDI10 extends ProjectWithCDITemplate{

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.AS());
	}
	
	public EjbProjectWithCDITestCDI10(){
		enabledByDefault = false;
		PROJECT_NAME = "EjbProject";
		ignoredProblem = "An EJB module must contain one or more enterprise beans";
		expectedProblemAdded = "Missing beans.xml file in the project";
	}
	
	@Before
	public void createEjbProject(){
		EjbProjectWizard dw = new EjbProjectWizard();
		dw.open();
		EjbProjectFirstPage fp = new EjbProjectFirstPage(dw);
		fp.setProjectName(PROJECT_NAME);
		dw.finish();
	}

}
