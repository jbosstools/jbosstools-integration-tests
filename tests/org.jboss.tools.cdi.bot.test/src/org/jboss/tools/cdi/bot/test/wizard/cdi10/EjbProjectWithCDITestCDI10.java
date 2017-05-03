package org.jboss.tools.cdi.bot.test.wizard.cdi10;

import java.util.Arrays;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jst.ejb.ui.project.facet.EjbProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.ejb.ui.project.facet.EjbProjectWizard;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.wizard.template.ProjectWithCDITemplate;
import org.junit.Before;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class EjbProjectWithCDITestCDI10 extends ProjectWithCDITemplate{
	
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
		EjbProjectFirstPage fp = new EjbProjectFirstPage();
		fp.setProjectName(PROJECT_NAME);
		dw.finish();
	}

}
