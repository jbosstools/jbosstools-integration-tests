/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.wizard.cdi20;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.condition.ProblemExists;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectFirstPage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.jre.JRERequirement.JRE;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.wizard.template.CDIWebProjectWizardTemplate;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIProjectWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.facet.CDIInstallWizardPage;
import org.junit.Test;

/**
 * 
 * @author zcervink@redhat.com
 * 
 */
@JRE(cleanup=true)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
public class CDIWebProjectWizardTestCDI20 extends CDIWebProjectWizardTemplate{

	@RequirementRestriction
	public static Collection<RequirementMatcher> getRestrictionMatcher() {
		if (CDITestBase.isJavaLE8()) { 
			return Arrays.asList(new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.WildFly()),
					new RequirementMatcher(JBossServer.class, VERSION, "16"));
		} else {
			return Arrays.asList(
					new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.WildFly()),
					new RequirementMatcher(JBossServer.class, VERSION, "16"),
					new RequirementMatcher(JRE.class, VERSION, "1.8"));
		}
	}
	
	public CDIWebProjectWizardTestCDI20(){
		CDIVersion = "2.0";
	}
	
	//JBIDE-26643 | cdi2.0 does not contain the page for unchecking the "create beans.xml" option
	@Test(expected = JBIDE26643Exception.class)
	public void createCDIProjectWithoutBeansXmlCDI20() throws JBIDE26643Exception{
		CDIProjectWizard cw = new CDIProjectWizard();
		cw.open();
		WebProjectFirstPage fp = new WebProjectFirstPage(cw);
		fp.setProjectName(PROJECT_NAME);
		if (CDIVersion.equals("2.0")) {
			new DefaultCombo(2).setSelection("Dynamic Web Project with CDI 2.0 (Contexts and Dependency Injection)");
		}
		assertEquals(sr.getRuntimeName(),fp.getTargetRuntime());
		assertEquals("Dynamic Web Project with CDI "+CDIVersion+" (Contexts and Dependency Injection)",fp.getConfiguration());
		fp.activateFacet("1.8", "Java");
		cw.next();
		cw.next();
		cw.next();
		try {
			CDIInstallWizardPage ip = new CDIInstallWizardPage(cw);
			ip.toggleCreateBeansXml(false);
		} catch (CoreLayerException e) {
			cw.cancel();
			throw new JBIDE26643Exception("'Create beans.xml' checkbox not found while using CDI 2.0.");
		}
		cw.finish();
		isCDISupportEnabled(PROJECT_NAME);
		isCDIFacetEnabled(PROJECT_NAME, CDIVersion);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		assertTrue(pe.containsProject(PROJECT_NAME));
		assertFalse(pe.getProject(PROJECT_NAME).containsResource("WebContent","WEB-INF","beans.xml"));
		new WaitUntil(new ProblemExists(ProblemType.ALL), TimePeriod.LONG, false);
		if (CDIVersion.equals("1.0")) {
			assertTrue(new ProblemExists(ProblemType.ALL).test());	
		} else {	
			new WaitWhile(new ProblemExists(ProblemType.ALL));	
		}
	}
	
	class JBIDE26643Exception extends Exception {
		
		public JBIDE26643Exception(String message) {
			super(message);
		}
	}
}
