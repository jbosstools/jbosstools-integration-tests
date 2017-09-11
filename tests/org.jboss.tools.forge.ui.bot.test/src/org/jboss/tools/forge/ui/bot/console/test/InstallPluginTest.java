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
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.junit.Test;

@CleanWorkspace
public class InstallPluginTest extends ForgeConsoleTestBase {

	
	@Test
	public void installJBossAS7Test(){
		final String ASSERT_TEXT =  "***SUCCESS*** Installed from " + 
									"[https://github.com/forge/plugin-jboss-as.git] " + 
									"successfully.";
		installPlugin("jboss-as-7");
		new WaitUntil(new ForgeConsoleHasText(ASSERT_TEXT), TimePeriod.getCustom(600)); //10m
		assertTrue(fView.getConsoleText().contains(ASSERT_TEXT));
	}
	
	@Test
	public void installHibernateToolsTest(){
		final String ASSERT_TEXT =  "***SUCCESS*** Installed from " + 
									"[https://github.com/forge/plugin-hibernate-tools.git] " + 
									"successfully.";
		installPlugin("hibernate-tools");
		new WaitUntil(new ForgeConsoleHasText(ASSERT_TEXT), TimePeriod.getCustom(600));//10m
		assertTrue(fView.getConsoleText().contains(ASSERT_TEXT));
	}		
}
