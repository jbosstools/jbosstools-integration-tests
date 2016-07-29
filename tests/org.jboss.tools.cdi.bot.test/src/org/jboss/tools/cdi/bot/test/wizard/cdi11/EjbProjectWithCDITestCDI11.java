package org.jboss.tools.cdi.bot.test.wizard.cdi11;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jst.ejb.ui.EjbProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.ejb.ui.EjbProjectWizard;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.wizard.template.ProjectWithCDITemplate;
import org.junit.Before;

@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY10x, cleanup=false)
public class EjbProjectWithCDITestCDI11 extends ProjectWithCDITemplate{
	
	public EjbProjectWithCDITestCDI11(){
		enabledByDefault = true;
		PROJECT_NAME = "EjbProject";
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
