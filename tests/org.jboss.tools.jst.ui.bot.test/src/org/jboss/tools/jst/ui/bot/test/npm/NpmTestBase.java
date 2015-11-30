/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.ui.bot.test.npm;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizardFirstPage;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.jst.reddeer.npm.ui.NpmInitDialog;
import org.junit.runner.RunWith;

/**
 * 
 * @author Pavol Srna
 *
 */
@RunWith(RedDeerSuite.class)
public class NpmTestBase {
	
	protected static final String PROJECT_NAME = "testProject";
	protected static final String WORKSPACE = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	protected static final String NPM_BASE_DIRECTORY = WORKSPACE + "/" + PROJECT_NAME;

	protected void createJSProject() {
		createJSProject(PROJECT_NAME);
	}

	protected void createJSProject(String name) {
		JavaProjectWizardDialog jsDialog = new JavaProjectWizardDialog();
		jsDialog.open();
		JavaProjectWizardFirstPage jsPage = new JavaProjectWizardFirstPage();
		jsPage.setName(name);
		jsDialog.finish();
		assertTrue("Project not found", new ProjectExplorer().containsProject(name));
	}
	
	protected void npmInit() {
		npmInit(PROJECT_NAME);
	}

	
	protected void npmInit(String projectName) {
		NpmInitDialog dialog = new NpmInitDialog();
		dialog.open();
		new LabeledText("Base directory:").setText(NPM_BASE_DIRECTORY);
		dialog.finish();
		assertPackageJsonExists();
	}
	
	protected void assertPackageJsonExists(){
		File packageJson = new File(NPM_BASE_DIRECTORY + "/package.json");
		assertTrue("package.json file does not exist", packageJson.exists());
	}

}
