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
package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.junit.Test;

/**
 * Tests of the Forge2 'project-new' wizard.
 * @author Pavol Srna
 *
 */
public class ProjectNewWizardTest extends WizardTestBase {
	 
	@Test
	public void testIsFocusedOnStartup(){
		WizardDialog wd = getWizardDialog("project-new", "(Project: New).*");
		assertTrue("'Project: New' wizard is not focused on startup", new DefaultShell().isFocusControl());
		wd.cancel();
	}
	
	@Test
	public void testIsProjectCreated(){
		newProject(PROJECT_NAME);
	}
	
	
	@Test
	public void testFinishBtnDisabled(){
		WizardDialog wd = getWizardDialog("project-new", "(Project: New).*");
		assertTrue(new LabeledText("Project name:").getText().isEmpty());
		assertFalse(new PushButton("Finish").isEnabled());
		new LabeledText("Project name:").setText(PROJECT_NAME);
		assertTrue(new PushButton("Finish").isEnabled());
		wd.cancel();
	}
	
	@Test
	public void testNewMavenResourcesProject(){
		WizardDialog wd = getWizardDialog("project-new", "(Project: New).*");
		new LabeledText("Project name:").setText(PROJECT_NAME);
		new LabeledText("Project location:").setText(WORKSPACE);
		new LabeledText("Top level package:").setText(GROUPID);
		new LabeledCombo("Project type:").setSelection("Java Resources (JAR)");
		wd.finish();
		//asserts
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue(pe.containsProject(PROJECT_NAME));
		try {
			String pomContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/pom.xml");
			assertTrue(pomContent.contains("<groupId>" + GROUPID +"</groupId>"));
			assertTrue(pomContent.contains("<artifactId>" + PROJECT_NAME +"</artifactId>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'pom.xml' failed!");
		}
	}
	
	@Test
	public void testNewWarProject(){
		WizardDialog wd = getWizardDialog("project-new", "(Project: New).*");
		new LabeledText("Project name:").setText(PROJECT_NAME);
		new LabeledText("Project location:").setText(WORKSPACE);
		new LabeledText("Top level package:").setText(GROUPID);
		new LabeledCombo("Project type:").setSelection("Java Web Application (WAR)");
		wd.finish();
		//asserts
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue(pe.containsProject(PROJECT_NAME));
		try {
			String pomContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/pom.xml");
			assertTrue(pomContent.contains("<groupId>" + GROUPID +"</groupId>"));
			assertTrue(pomContent.contains("<artifactId>" + PROJECT_NAME +"</artifactId>"));
			assertTrue(pomContent.contains("<packaging>war</packaging>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'pom.xml' failed!");
		}
	}
	
}
