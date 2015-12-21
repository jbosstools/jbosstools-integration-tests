package org.jboss.tools.cdi.bot.test.wizard.cdi11;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.wizard.template.ProjectWithCDITemplate;
import org.junit.Before;

@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class DynamicWebProjectWithCDITestCDI11 extends ProjectWithCDITemplate{
	
	public DynamicWebProjectWithCDITestCDI11(){
		enabledByDefault = true;
	}
	
	@Before
	public void createWebProject(){
		WebProjectWizard dw = new WebProjectWizard();
		dw.open();
		WebProjectFirstPage fp = new WebProjectFirstPage();
		fp.setProjectName(PROJECT_NAME);
		dw.finish();
	}

}
