/*******************************************************************************
 * Copyright (c) 2010-2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beans.ibinding.cdi11;

import java.util.Collection;

import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.jre.JRERequirement.JRE;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.beans.ibinding.template.IBindingValidationQuickFixTemplate;
import org.jboss.tools.cdi.reddeer.validators.InterceptorBindingValidationProvider;
import org.junit.Before;

@JRE(cleanup=true)
@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class IBindingValidationQuickFixTestCDI11 extends IBindingValidationQuickFixTemplate{

	@RequirementRestriction
	public static Collection<RequirementMatcher> getRestrictionMatcher() {
		return getRestrictionMatcherCDI11();
	}
	
	public IBindingValidationQuickFixTestCDI11() {
		CDIVersion = "1.2";
	}
		
	@Before
	public void changeDiscoveryMode(){
		validationProvider = new InterceptorBindingValidationProvider("JSR-346");
		prepareBeanXml("all", true);
	}

}
