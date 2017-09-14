package org.jboss.tools.cdi.bot.test.weld.cdi11;

import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.weld.template.WeldExcludeTemplate;
import org.jboss.tools.cdi.reddeer.matcher.ServerMatcher;
import org.jboss.tools.cdi.reddeer.validators.BeanValidationProviderCDI11;
import org.junit.Before;


@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
public class WeldExcludeTestCDI11 extends WeldExcludeTemplate{

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.WildFly());
	}
	
	@Before
	public void setValidationProvider(){
		validationProvider = new BeanValidationProviderCDI11();
		prepareBeanXml("all", false);
	}

}
