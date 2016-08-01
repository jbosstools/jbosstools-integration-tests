package org.jboss.tools.cdi.bot.test.wizard.cdi11;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jst.j2ee.ui.project.facet.UtilityProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.j2ee.ui.project.facet.UtilityProjectWizard;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.wizard.template.ProjectWithCDITemplate;
import org.junit.Before;


@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY10x, cleanup=false)
public class UtilityProjectWithCDITestCDI11 extends ProjectWithCDITemplate{
	
	public UtilityProjectWithCDITestCDI11(){
		enabledByDefault = true;
		PROJECT_NAME = "UtilityProject";
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
