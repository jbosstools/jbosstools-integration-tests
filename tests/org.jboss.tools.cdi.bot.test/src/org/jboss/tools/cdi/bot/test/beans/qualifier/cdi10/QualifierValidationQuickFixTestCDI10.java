package org.jboss.tools.cdi.bot.test.beans.qualifier.cdi10;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.beans.qualifier.template.QualifierValidationQuickFixTemplate;
import org.jboss.tools.cdi.reddeer.validators.QualifierValidationProviderCDI10;
import org.junit.Before;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
public class QualifierValidationQuickFixTestCDI10 extends QualifierValidationQuickFixTemplate {
	
	@Before
	public void setProvider(){
		validationProvider = new QualifierValidationProviderCDI10();
		CDIVersion = "1.0";
	}

}
