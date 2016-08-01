package org.jboss.tools.cdi.bot.test.beans.stereotype.cdi10;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.beans.stereotype.template.StereotypeValidationQuickFixTemplate;
import org.jboss.tools.cdi.reddeer.validators.StereotypeValidationProviderCDI10;
import org.junit.Before;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class StereotypeValidationQuickFixTestCDI10 extends StereotypeValidationQuickFixTemplate{
	
	@Before
	public void setProvider(){
		validationProvider = new StereotypeValidationProviderCDI10();
		CDIVersion = "1.0";
	}

}
