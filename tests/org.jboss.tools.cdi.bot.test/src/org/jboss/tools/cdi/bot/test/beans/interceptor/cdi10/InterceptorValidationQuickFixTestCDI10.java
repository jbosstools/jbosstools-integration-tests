package org.jboss.tools.cdi.bot.test.beans.interceptor.cdi10;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.beans.interceptor.template.InterceptorValidationQuickFixTemplate;
import org.jboss.tools.cdi.reddeer.validators.InterceptorValidationProviderCDI10;
import org.junit.Before;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class InterceptorValidationQuickFixTestCDI10 extends InterceptorValidationQuickFixTemplate{
	
	@Before
	public void setProvider(){
		validationProvider = new InterceptorValidationProviderCDI10();
	}

}
