/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beansxml.cdi11;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.jre.JRERequirement.JRE;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.beansxml.template.BeansXMLUITemplate;
import org.jboss.tools.cdi.reddeer.validators.BeansXmlUIValidationProviderCDI11;
import org.junit.Before;

@JRE(cleanup=true)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state = ServerRequirementState.PRESENT, cleanup = false)
public class BeansXMLUITestCDI11 extends BeansXMLUITemplate {

	@RequirementRestriction
	public static Collection<RequirementMatcher> getRestrictionMatcher() {
		if (isJavaLE8()) { 
			return Arrays.asList(new RequirementMatcher(JBossServer.class, "family", ServerMatcher.WildFly()));
		} else {
			return Arrays.asList(
					new RequirementMatcher(JBossServer.class, "family", ServerMatcher.WildFly()),
					new RequirementMatcher(JRE.class, "version", "1.8"));
		}
	}

	@Before
	public void setCDIVersion() {
		CDIVersion = "1.1";
		validationProvider = new BeansXmlUIValidationProviderCDI11();
	}
}
