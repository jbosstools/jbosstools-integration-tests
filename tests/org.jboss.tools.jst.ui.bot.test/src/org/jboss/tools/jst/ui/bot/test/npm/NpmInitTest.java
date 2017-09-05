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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.jst.reddeer.npm.ui.NpmInitDialog;
import org.jboss.tools.jst.ui.bot.test.JSTTestBase;
import org.eclipse.reddeer.core.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Pavol Srna
 *
 */
public class NpmInitTest extends JSTTestBase {

	private static final String NPM_PROPERTY_NAME = "TestName";
	private static final String NPM_PROPERTY_VERSION = "1.1.1";
	private static final String NPM_PROPERTY_LICENSE = "GPL";

	@Before
	public void prepare() {
		createJSProject(PROJECT_NAME);
	}

	@After
	public void cleanup() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
	}

	@Test
	public void testPackageJsonCreated() {
		npmInit(PROJECT_NAME);
		assertPackageJsonExists();
	}

	@Test
	public void testPackageJsonOpenedInEditor() {
		npmInit(PROJECT_NAME);
		DefaultEditor editor = new DefaultEditor();
		assertTrue("Editor is not active", editor.isActive());
		assertThat(editor.getTitle(), is("package.json"));
	}

	@Test
	public void testWizardRejectsWhenJsonFileExists() {
		npmInit(PROJECT_NAME);
		NpmInitDialog npmDialog = new NpmInitDialog();
		npmDialog.open();
		new LabeledText("Base directory:").setText(BASE_DIRECTORY);
		assertFalse("Finish button not disabled", npmDialog.isFinishEnabled());
	}

	@Test
	public void testWizardRejectsEmptyBaseDir() {
		NpmInitDialog npmDialog = new NpmInitDialog();
		npmDialog.open();
		new LabeledText("Base directory:").setText(BASE_DIRECTORY);
		assertTrue("Finish button not enabled", npmDialog.isFinishEnabled());
		new LabeledText("Base directory:").setText("");
		assertFalse("Finish button not disabled", npmDialog.isFinishEnabled());
	}

	@Test
	public void testWizardRejectsNonExistingDir() {
		NpmInitDialog npmDialog = new NpmInitDialog();
		npmDialog.open();
		new LabeledText("Base directory:").setText(BASE_DIRECTORY);
		assertTrue("Finish button not enabled", npmDialog.isFinishEnabled());
		new LabeledText("Base directory:").setText(BASE_DIRECTORY + "XYZ");
		assertFalse("Finish button not disabled", npmDialog.isFinishEnabled());
	}

	@Test
	public void testCanEditNpmConfigProperties() throws IOException {
		NpmInitDialog npmDialog = new NpmInitDialog();
		npmDialog.open();
		new LabeledText("Base directory:").setText(BASE_DIRECTORY);
		new CheckBox("Use default configuration").toggle(false);
		new LabeledText("Name:").setText(NPM_PROPERTY_NAME);
		new LabeledText("Version:").setText(NPM_PROPERTY_VERSION);
		new LabeledText("License:").setText(NPM_PROPERTY_LICENSE);
		npmDialog.finish();
		assertPackageJsonExists();
		String packageJson = FileUtil.readFile(BASE_DIRECTORY + "/package.json");
		assertTrue("package.json has incorrect content",
				packageJson.contains("\"name\": \"" + NPM_PROPERTY_NAME + "\""));
		assertTrue("package.json has incorrect content",
				packageJson.contains("\"version\": \"" + NPM_PROPERTY_VERSION + "\""));
		assertTrue("package.json has incorrect content",
				packageJson.contains("\"license\": \"" + NPM_PROPERTY_LICENSE + "\""));
	}

}
