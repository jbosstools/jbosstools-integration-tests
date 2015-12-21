package org.jboss.tools.cdi.bot.test.beans.decorator.cdi10;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.beans.decorator.template.DecoratorValidationQuickFixTemplate;
import org.jboss.tools.cdi.reddeer.validators.DecoratorValidationProviderCDI10;
import org.junit.Before;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
public class DecoratorValidationQuickFixTestCDI10 extends DecoratorValidationQuickFixTemplate{
	
	@Before
	public void setProvider(){
		validationProvider = new DecoratorValidationProviderCDI10();
	}

}
