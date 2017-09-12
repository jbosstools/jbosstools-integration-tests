package org.jboss.tools.cdi.bot.test.beansxml.validation.cdi11;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.beansxml.validation.template.BeansXMLValidationQuickFixTemplate;
import org.jboss.tools.cdi.reddeer.matcher.ServerMatcher;
import org.jboss.tools.cdi.reddeer.validators.BeansXmlValidationProviderCDI11;
import org.junit.Before;

@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class BeansXMLValidationQuickFixTestCDI11 extends BeansXMLValidationQuickFixTemplate{

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.WildFly());
	}
	
	@Before
	public void setValidationProvider(){
		validationProvider = new BeansXmlValidationProviderCDI11();
		requireBeansXML = false;
		setBeanDiscoveryMode("all");
		new WaitWhile(new JobIsRunning());
	}

}
