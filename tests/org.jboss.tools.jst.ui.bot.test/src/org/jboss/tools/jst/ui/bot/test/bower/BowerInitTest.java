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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.jst.reddeer.bower.ui.BowerInitDialog;
import org.jboss.reddeer.core.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Pavol Srna
 *
 */
public class BowerInitTest extends BowerTestBase {

	private static final String BOWER_PROPERTY_NAME = "TestName";
	private static final String BOWER_PROPERTY_VERSION = "1.1.1";
	private static final String BOWER_PROPERTY_LICENSE = "GPL";

	@Before
	public void prepare() {
		createJSProject(PROJECT_NAME);
	}

	@After
	public void cleanup() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
	}

	@Test
	public void testBowerJsonCreated() {
		bowerInit(PROJECT_NAME);
		assertBowerJsonExists();
	}

	@Test
	public void testBowerJsonOpenedInEditor() {
		bowerInit(PROJECT_NAME);
		DefaultEditor editor = new DefaultEditor();
		assertTrue("Editor is not active", editor.isActive());
		assertThat(editor.getTitle(), is("bower.json"));
	}

	@Test
	public void testWizardRejectsWhenJsonFileExists() {
		bowerInit(PROJECT_NAME);
		BowerInitDialog bowerDialog = new BowerInitDialog();
		bowerDialog.open();
		new LabeledText("Base directory:").setText(BOWER_BASE_DIRECTORY);
		assertFalse("Finish button not disabled", bowerDialog.isFinishEnabled());
	}

	@Test
	public void testWizardRejectsEmptyBaseDir() {
		BowerInitDialog bowerDialog = new BowerInitDialog();
		bowerDialog.open();
		new LabeledText("Base directory:").setText(BOWER_BASE_DIRECTORY);
		assertTrue("Finish button not enabled", bowerDialog.isFinishEnabled());
		new LabeledText("Base directory:").setText("");
		assertFalse("Finish button not disabled", bowerDialog.isFinishEnabled());
	}

	@Test
	public void testWizardRejectsNonExistingDir() {
		BowerInitDialog bowerDialog = new BowerInitDialog();
		bowerDialog.open();
		new LabeledText("Base directory:").setText(BOWER_BASE_DIRECTORY);
		assertTrue("Finish button not enabled", bowerDialog.isFinishEnabled());
		new LabeledText("Base directory:").setText(BOWER_BASE_DIRECTORY + "XYZ");
		assertFalse("Finish button not disabled", bowerDialog.isFinishEnabled());
	}

	@Test
	public void testCanEditBowerConfigProperties() throws IOException {
		BowerInitDialog bowerDialog = new BowerInitDialog();
		bowerDialog.open();
		new LabeledText("Base directory:").setText(BOWER_BASE_DIRECTORY);
		new CheckBox("Use default configuration").toggle(false);
		new LabeledText("Name:").setText(BOWER_PROPERTY_NAME);
		new LabeledText("Version:").setText(BOWER_PROPERTY_VERSION);
		new LabeledText("License:").setText(BOWER_PROPERTY_LICENSE);
		bowerDialog.finish();
		assertBowerJsonExists();
		String bowerJson = FileUtil.readFile(BOWER_BASE_DIRECTORY + "/bower.json");
		assertTrue("bower.json has incorrect content",
				bowerJson.contains("\"name\": \"" + BOWER_PROPERTY_NAME + "\""));
		assertTrue("bower.json has incorrect content",
				bowerJson.contains("\"version\": \"" + BOWER_PROPERTY_VERSION + "\""));
		assertTrue("bower.json has incorrect content",
				bowerJson.contains("\"license\": \"" + BOWER_PROPERTY_LICENSE + "\""));
	}

}
