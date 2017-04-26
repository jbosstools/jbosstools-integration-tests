/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.forge.ui.bot.console.test;

import static org.junit.Assert.assertTrue;

import org.jboss.tools.forge.reddeer.condition.ForgeConsoleHasText;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.junit.Test;
/**
 * 
 * @author psrna
 *
 */
@CleanWorkspace
public class ScaffoldingTest extends ForgeConsoleTestBase {

	private static final String specJPAText =     "***SUCCESS*** Installed [forge.spec.jpa] successfully.";
	private static final String specEJBText =     "***SUCCESS*** Installed [forge.spec.ejb] successfully.";
	private static final String specCDIText =     "***SUCCESS*** Installed [forge.spec.cdi] successfully.";
	private static final String specJsfApiText =  "***SUCCESS*** Installed [forge.spec.jsf.api] successfully.";
	private static final String facesText =       "***SUCCESS*** Installed [faces] successfully.";
	
	@Test
	public void scaffoldSetupTest(){
	
		createProject(ProjectTypes.war);
		fView.setConsoleText("scaffold setup --scaffoldType faces\n");
		fView.setConsoleText("\n"); // install scaffold provider [faces]
		fView.setConsoleText("\n"); //default version of 'jboss-javaee-6.0'
		
		new WaitUntil(new ForgeConsoleHasText(specJPAText), TimePeriod.VERY_LONG);
		assertTrue(fView.getConsoleText().contains(specJPAText));
		new WaitUntil(new ForgeConsoleHasText(specEJBText), TimePeriod.VERY_LONG);
		assertTrue(fView.getConsoleText().contains(specEJBText));
		new WaitUntil(new ForgeConsoleHasText(specCDIText), TimePeriod.VERY_LONG);
		assertTrue(fView.getConsoleText().contains(specCDIText));
		new WaitUntil(new ForgeConsoleHasText(specJsfApiText), TimePeriod.VERY_LONG);
		assertTrue(fView.getConsoleText().contains(specJsfApiText));
		new WaitUntil(new ForgeConsoleHasText(facesText), TimePeriod.VERY_LONG);
		assertTrue(fView.getConsoleText().contains(facesText));
		
		fView.setConsoleText("\n"); //subdirectory of web-root
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
}
