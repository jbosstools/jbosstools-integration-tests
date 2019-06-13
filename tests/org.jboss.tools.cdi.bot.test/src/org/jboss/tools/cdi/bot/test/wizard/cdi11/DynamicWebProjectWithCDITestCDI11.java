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
package org.jboss.tools.cdi.bot.test.wizard.cdi11;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectWizard;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.jre.JRERequirement.JRE;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.wizard.template.ProjectWithCDITemplate;
import org.junit.Before;

@JRE(cleanup=true)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
public class DynamicWebProjectWithCDITestCDI11 extends ProjectWithCDITemplate{

	@RequirementRestriction
	public static Collection<RequirementMatcher> getRestrictionMatcher() {
		if (CDITestBase.isJavaLE8()) { 
			return Arrays.asList(new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.WildFly()),
					new RequirementMatcher(JBossServer.class, VERSION, "13"));
		} else {
			return Arrays.asList(
					new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.WildFly()),
					new RequirementMatcher(JBossServer.class, VERSION, "13"),
					new RequirementMatcher(JRE.class, VERSION, "1.8"));
		}
	}
	
	public DynamicWebProjectWithCDITestCDI11(){
		enabledByDefault = true;
	}
	
	@Before
	public void createWebProject(){
		WebProjectWizard dw = new WebProjectWizard();
		dw.open();
		WebProjectFirstPage fp = new WebProjectFirstPage(dw);
		fp.setProjectName(PROJECT_NAME);
		fp.activateFacet("1.8", "Java");
		dw.finish();
	}

}
