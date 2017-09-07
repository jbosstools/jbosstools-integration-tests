package org.jboss.tools.cdi.bot.test.wizard.cdi10;

import java.util.Arrays;

import org.eclipse.reddeer.eclipse.jst.ejb.ui.project.facet.EjbProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.ejb.ui.project.facet.EjbProjectWizard;
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
public class EjbProjectWithCDITestCDI10 extends ProjectWithCDITemplate{

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.Eap());
	}
	
	public EjbProjectWithCDITestCDI10(){
		enabledByDefault = false;
		PROJECT_NAME = "EjbProject";
		expectedProblem = "An EJB module must contain one or more enterprise beans";
		expectedProblemRemoved = "An EJB module must contain one or more enterprise beans";
		expectedProblemAdded = Arrays.asList("An EJB module must contain one or more enterprise beans",
				"Missing beans.xml file in the project");
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
