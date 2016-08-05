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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.workbench.condition.EditorWithTitleIsActive;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.junit.Before;
import org.junit.Test;

public class RESTSetupWizardTest extends WizardTestBase {

	private static String JAX_RS_VERSION = "2.0";
	private static String APP_PATH = "/rest";
	private static String CLASS_NAME = "RestApp";
	private static String DEPENDENCY = "javax.ws.rs-api : " + JAX_RS_VERSION
			+ " [provided]";

	@Before
	public void prepare() {
		newProject(PROJECT_NAME);
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(PROJECT_NAME); // this will set context for forge
		WizardDialog wd = getWizardDialog("rest-setup", "(REST: Setup).*");
		new LabeledCombo("JAX-RS Version:").setSelection(JAX_RS_VERSION);
		new LabeledText("Application Path:").setText(APP_PATH);
		new LabeledText("Target Package:").setText("org.test");
		new LabeledText("Class Name:").setText(CLASS_NAME);
		wd.finish(TimePeriod.getCustom(600));
	}

	@Test
	public void testDependenciesAddedToPom() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.getProject(PROJECT_NAME).getTreeItem().getItem("pom.xml")
				.doubleClick();
		new WaitUntil(new EditorWithTitleIsActive(PROJECT_NAME + "/pom.xml"));
		new DefaultEditor(PROJECT_NAME + "/pom.xml").activate();
		new DefaultCTabItem("Dependencies").activate();
		assertTrue("Dependency: '" + DEPENDENCY + "' not added!",
				new DefaultTable(1).containsItem(DEPENDENCY));
	}

	@Test
	public void testAppJavaFileCreated() {
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue(
				CLASS_NAME + ".java has not been created!",
				pe.getProject(PROJECT_NAME).containsItem("src", "main", "java",
						"org", "test", CLASS_NAME + ".java"));
		File javaFile = new File(WORKSPACE + "/" + PROJECT_NAME
				+ "/src/main/java/org/test/" + CLASS_NAME + ".java");
		assertTrue(CLASS_NAME + ".java has not been created!",
				javaFile.exists());
	}

	@Test
	public void testAppPathDefined() {
		try {
			String javaContent = ResourceUtils.readFile(WORKSPACE + "/"
					+ PROJECT_NAME + "/src/main/java/org/test/" + CLASS_NAME
					+ ".java");
			assertTrue(javaContent.contains("@ApplicationPath(\"" + APP_PATH
					+ "\")"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'pom.xml' failed!");
		}
	}
}
