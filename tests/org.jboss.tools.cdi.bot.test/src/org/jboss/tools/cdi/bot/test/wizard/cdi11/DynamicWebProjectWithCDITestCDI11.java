package org.jboss.tools.cdi.bot.test.wizard.cdi11;

import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectWizard;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.wizard.template.ProjectWithCDITemplate;
import org.jboss.tools.cdi.reddeer.matcher.ServerMatcher;
import org.junit.Before;

@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
public class DynamicWebProjectWithCDITestCDI11 extends ProjectWithCDITemplate{

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.WildFly());
	}
	
	public DynamicWebProjectWithCDITestCDI11(){
		enabledByDefault = true;
	}
	
	@Before
	public void createWebProject(){
		WebProjectWizard dw = new WebProjectWizard();
		dw.open();
		WebProjectFirstPage fp = new WebProjectFirstPage(dw);
		fp.setProjectName(PROJECT_NAME);
		dw.finish();
	}

}
