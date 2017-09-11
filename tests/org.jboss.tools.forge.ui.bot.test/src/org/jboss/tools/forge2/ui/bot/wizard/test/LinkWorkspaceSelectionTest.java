/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.jboss.tools.forge.reddeer.condition.ForgeConsoleHasNoChange;
import org.jboss.tools.forge.reddeer.view.ForgeConsoleView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva@redhat.com
 *
 */

public class LinkWorkspaceSelectionTest extends WizardTestBase {

	@Before
	public void prepare() {
		newProject(PROJECT_NAME);
	}

	@After
	public void clearAndCleanUp() {
		ForgeConsoleView forgeConsoleView = new ForgeConsoleView();
		forgeConsoleView.open();
		forgeConsoleView.clear();
		forgeConsoleView.linkWithEditorToggle(false);
		setFocusOnProject();
		try {
			handleCleanup();
		} catch (SWTLayerException ex) {
			//On some slaves its not selected project immediately, instead is selected file
			new OkButton().click();
			handleCleanup();
		}
	}

	@Test
	public void testContextShowInForgeConsole() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(PROJECT_NAME); // this will set context for forge
		pe.getProject(PROJECT_NAME).getProjectItem("pom.xml").select();
		new ContextMenuItem("Show In", "Forge Console").select();
		ForgeConsoleView forgeConsoleView = new ForgeConsoleView();
		forgeConsoleView.open();
		new WaitWhile(new ForgeConsoleHasNoChange(),false);
		String consoleText = forgeConsoleView.getConsoleText();
		assertTrue(consoleText.endsWith("[pom.xml]$ "));
		setFocusOnProject();
	}

	@Test
	public void testLinkWithEditor() {
		cdiSetup(PROJECT_NAME, "1.1");
		ForgeConsoleView forgeConsoleView = new ForgeConsoleView();
		forgeConsoleView.open();
		forgeConsoleView.linkWithEditorToggle(true);
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(PROJECT_NAME); // this will set context for forge

		pe.getProject(PROJECT_NAME).getProjectItem("src", "main", "webapp", "WEB-INF", "beans.xml").open();
		forgeConsoleView.open();
		new WaitUntil(new ForgeConsoleHasNoChange(), TimePeriod.LONG);
		forgeConsoleView = new ForgeConsoleView();
		forgeConsoleView.open();
		String consoleText = forgeConsoleView.getConsoleText();
		assertTrue(consoleText.endsWith("[beans.xml]$ "));
		pe.getProject(PROJECT_NAME).select();
		pe.getProject(PROJECT_NAME).collapse();
		setFocusOnProject();
	}

}
