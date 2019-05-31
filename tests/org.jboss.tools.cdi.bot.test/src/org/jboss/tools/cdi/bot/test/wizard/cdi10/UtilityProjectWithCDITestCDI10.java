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
package org.jboss.tools.cdi.bot.test.wizard.cdi10;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.reddeer.eclipse.jst.j2ee.ui.project.facet.UtilityProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.j2ee.ui.project.facet.UtilityProjectWizard;
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
@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class UtilityProjectWithCDITestCDI10 extends ProjectWithCDITemplate{

	@RequirementRestriction
	public static Collection<RequirementMatcher> getRestrictionMatcher() {
		if (CDITestBase.isJavaLE8()) { 
			return Arrays.asList(new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.AS()));
		} else {
			return Arrays.asList(
					new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.AS()),
					new RequirementMatcher(JRE.class, VERSION, "1.8"));
		}
	}
	
	public UtilityProjectWithCDITestCDI10(){
		enabledByDefault = true;
		PROJECT_NAME = "UtilityProject";
		expectedProblemAdded = "Missing beans.xml file in the project";
	}
	
	@Before
	public void createUtilityProject(){
		UtilityProjectWizard uw = new UtilityProjectWizard();
		uw.open();
		UtilityProjectFirstPage up = new UtilityProjectFirstPage(uw);
		up.setProjectName(PROJECT_NAME);
		up.activateFacet("1.8", "Java");
		uw.finish();
	}

}
