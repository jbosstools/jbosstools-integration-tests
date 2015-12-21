package org.jboss.tools.cdi.bot.test.beans.scope.cdi10;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.beans.scope.template.ScopeValidationQuickFixTemplate;
import org.jboss.tools.cdi.reddeer.validators.ScopeValidationProviderCDI10;
import org.junit.Before;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
public class ScopeValidationQuickFixTestCDI10 extends ScopeValidationQuickFixTemplate{
	
	@Before
	public void setProvider(){
		validationProvider = new ScopeValidationProviderCDI10();
		CDIVersion = "1.0";
	}


}
