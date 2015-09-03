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
package org.jboss.tools.jst.ui.bot.test.bower;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizardFirstPage;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.jst.reddeer.bower.ui.BowerInitDialog;
import org.junit.runner.RunWith;

/**
 * 
 * @author Pavol Srna
 *
 */
@RunWith(RedDeerSuite.class)
public class BowerTestBase {

	protected static final String PROJECT_NAME = "testProject";
	protected static final String WORKSPACE = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	protected static final String BOWER_BASE_DIRECTORY = WORKSPACE + "/" + PROJECT_NAME;

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

	protected void bowerInit() {
		bowerInit(PROJECT_NAME);
	}

	protected void bowerInit(String projectName) {
		BowerInitDialog dialog = new BowerInitDialog();
		dialog.open();
		new LabeledText("Base directory:").setText(BOWER_BASE_DIRECTORY);
		dialog.finish();
		assertBowerJsonExists();
	}
	
	protected void assertBowerJsonExists(){
		File bowerJson = new File(BOWER_BASE_DIRECTORY + "/bower.json");
		assertTrue("bower.json file does not exist", bowerJson.exists());
	}

}
