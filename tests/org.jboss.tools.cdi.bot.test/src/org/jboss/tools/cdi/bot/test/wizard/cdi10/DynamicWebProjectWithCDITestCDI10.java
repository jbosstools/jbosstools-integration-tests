package org.jboss.tools.cdi.bot.test.wizard.cdi10;

import java.util.Arrays;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.wizard.template.ProjectWithCDITemplate;
import org.junit.Before;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class DynamicWebProjectWithCDITestCDI10 extends ProjectWithCDITemplate{
	
	public DynamicWebProjectWithCDITestCDI10(){
		enabledByDefault = false;
		expectedProblemAdded = Arrays.asList("Missing beans.xml file in the project");
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
