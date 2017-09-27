package org.jboss.tools.cdi.bot.test.beansxml.cdi11;

import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.beansxml.template.BeansXMLDiscoveryModesTemplate;

@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
public class BeansXMLDiscoveryModesTestCDI11 extends BeansXMLDiscoveryModesTemplate {

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.WildFly());
	}
}
