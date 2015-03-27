package org.jboss.tools.cdi.bot.test.beans.ibinding.cdi10;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.beans.ibinding.template.IBindingValidationQuickFixTemplate;
import org.jboss.tools.cdi.reddeer.validators.InterceptorBindingValidationProviderCDI10;
import org.junit.Before;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class IBindingValidationQuickFixTestCDI10 extends IBindingValidationQuickFixTemplate{
	
	@Before
	public void setProvider(){
		validationProvider = new InterceptorBindingValidationProviderCDI10();
	}

}
