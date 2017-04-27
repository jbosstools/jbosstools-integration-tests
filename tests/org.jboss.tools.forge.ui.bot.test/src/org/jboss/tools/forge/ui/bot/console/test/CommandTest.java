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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.swt.SWT;
import org.jboss.tools.forge.reddeer.condition.ForgeConsoleHasText;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.junit.Test;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;

@CleanWorkspace
public class CommandTest extends ForgeConsoleTestBase {

	@Test
	public void cdWorkspaceTest(){
		createProject();
		assertTrue(!pwd().equals(WORKSPACE));
		cdWS();
		assertTrue(pwd().equals(WORKSPACE));
	}
	 
	@Test
	public void cdHomeTest(){
		String userHome = System.getProperty("user.home");	
		assertTrue(!pwd().equals(userHome));
		fView.setConsoleText("cd $\n");
		assertTrue(pwd().equals(userHome));
	}
	
	
	@Test
	public void pickupDirectoryTest(){
		String ASSERT_TEXT = "Picked up type <DirectoryResource>: main";
		createProject();
		cdWS();
		fView.setConsoleText("pick-up " + PROJECT_NAME + "/src/main" + "\n");
		new WaitUntil(new ForgeConsoleHasText(ASSERT_TEXT), TimePeriod.VERY_LONG);
		assertTrue(fView.getConsoleText().contains(ASSERT_TEXT));
		
		ProjectExplorer pe = new ProjectExplorer();
		Project project = pe.getProject(PROJECT_NAME);
		ProjectItem item = project.getProjectItem("src", "main");
		assertTrue(item.isSelected());
	}
	
	
	@Test
	public void pickupFileTest(){	
		String ASSERT_TEXT = "Picked up type <MavenPomResource>: pom.xml";
		createProject();
		cdWS();
		fView.setConsoleText("pick-up " + PROJECT_NAME + "/pom.xml" + "\n");
		new WaitUntil(new ForgeConsoleHasText(ASSERT_TEXT), TimePeriod.VERY_LONG);
		assertTrue(fView.getConsoleText().contains(ASSERT_TEXT));
		
		DefaultEditor e = new DefaultEditor();
		assertTrue("Editor is not active", e.isActive());
		assertTrue(e.getTitle().equals(PROJECT_NAME + "/pom.xml"));
		e.close();
	}
	
	
	@Test
	public void abortCommandTest() throws ParseException{
		String ASSERT_TEXT = "***INFO*** Aborted.";
		fView.setConsoleText("new-project --named " + PROJECT_NAME + "\n");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CTRL, SWT.SHIFT, 'C');
		new WaitUntil(new ForgeConsoleHasText(ASSERT_TEXT), TimePeriod.VERY_LONG);
		assertTrue(fView.getConsoleText().contains(ASSERT_TEXT));
		ProjectExplorer pe = new ProjectExplorer();
		assertFalse(pe.containsProject(PROJECT_NAME));
	}
	
	
	@Test
	public void showInForgeConsoleTest(){
		String ASSERT_TEXT = "Picked up type <DirectoryResource>: " + PROJECT_NAME;
		createProject();
		cdWS();
		Project project = pExplorer.getProject(PROJECT_NAME);
		project.select();
		new ContextMenu("Show In", "Forge Console").select();
		new WaitUntil(new ForgeConsoleHasText(ASSERT_TEXT), TimePeriod.VERY_LONG);
		assertTrue(fView.getConsoleText().contains(ASSERT_TEXT));
	}
	
}