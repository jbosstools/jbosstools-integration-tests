/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.ui.bot.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.dialogs.ExplorerItemPropertyDialog;
import org.jboss.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizardFirstPage;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.jst.reddeer.bower.ui.BowerInitDialog;
import org.jboss.tools.jst.reddeer.npm.ui.NpmInitDialog;
import org.jboss.tools.jst.reddeer.tern.ui.TernModulesPropertyPage;
import org.jboss.tools.jst.reddeer.wst.jsdt.ui.wizard.NewJSFileWizardDialog;
import org.jboss.tools.jst.reddeer.wst.jsdt.ui.wizard.NewJSFileWizardPage;
import org.junit.runner.RunWith;

/**
 * TestBase Class for Tern tests
 * 
 * @author Pavol Srna
 */
@RunWith(RedDeerSuite.class)
public class JSTTestBase {

	protected static final String PROJECT_NAME = "testProject";
	protected static final String JS_FILE = "main.js";
	protected static final String WORKSPACE = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	protected static final String BASE_DIRECTORY = WORKSPACE + "/" + PROJECT_NAME;

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

	protected void cleanEditor() {
		cleanEditor(JS_FILE);
	}

	protected void cleanEditor(String name) {
		DefaultEditor editor = new DefaultEditor(name);
		editor.activate();
		DefaultStyledText text = new DefaultStyledText();
		text.setText("");
		editor.save();
	}

	protected void createJSFile(String filename) {
		NewWizardDialog d = new NewJSFileWizardDialog();
		d.open();
		NewJSFileWizardPage p = new NewJSFileWizardPage();
		new LabeledText("Enter or select the parent folder:").setText(PROJECT_NAME);
		p.setFileName(filename);
		d.finish();
		assertTrue(filename + " not found!", new ProjectExplorer().getProject(PROJECT_NAME).containsItem(filename));
	}

	protected void setTernModule(String module) {
		setTernModule(module, PROJECT_NAME);
	}

	protected void setTernModule(String module, String project) {
		TernModulesPropertyPage propPage = new TernModulesPropertyPage();
		ExplorerItemPropertyDialog dialog = openProjectProperties(project);
		dialog.select(propPage);
		new DefaultTable().getItem(module).setChecked(true);
		dialog.ok();
	}

	protected ExplorerItemPropertyDialog openProjectProperties() {
		return openProjectProperties(PROJECT_NAME);
	}

	protected ExplorerItemPropertyDialog openProjectProperties(String projectName) {
		ProjectExplorer pe = new ProjectExplorer();
		ExplorerItemPropertyDialog dialog = new ExplorerItemPropertyDialog(pe.getProject(projectName));
		dialog.open();
		Shell shell = new DefaultShell();
		assertThat(shell.getText(), is(dialog.getTitle()));

		return dialog;
	}

	protected static String getMisingString(List<String> current, List<String> expected) {
		StringBuffer sbMissing = new StringBuffer("");
		for (String expectedItem : expected) {
			if (!current.contains(expectedItem)) {
				if (sbMissing.length() != 0) {
					sbMissing.append(",");
				}
				sbMissing.append(expectedItem);
			}
		}
		return sbMissing.toString();
	}

	protected void npmInit() {
		npmInit(PROJECT_NAME);
	}

	protected void npmInit(String projectName) {
		NpmInitDialog dialog = new NpmInitDialog();
		dialog.open();
		new LabeledText("Base directory:").setText(BASE_DIRECTORY);
		dialog.finish();
		assertPackageJsonExists();
	}

	protected void assertPackageJsonExists() {
		File packageJson = new File(BASE_DIRECTORY + "/package.json");
		assertTrue("package.json file does not exist", packageJson.exists());
	}

	protected void bowerInit() {
		bowerInit(PROJECT_NAME);
	}

	protected void bowerInit(String projectName) {
		BowerInitDialog dialog = new BowerInitDialog();
		dialog.open();
		new LabeledText("Base directory:").setText(BASE_DIRECTORY);
		dialog.finish();
		assertBowerJsonExists();
	}

	protected void assertBowerJsonExists() {
		File bowerJson = new File(BASE_DIRECTORY + "/bower.json");
		assertTrue("bower.json file does not exist", bowerJson.exists());
	}

	protected void bowerUpdate(String projectName) {
		new ProjectExplorer().getProject(projectName).select();
		new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Bower Update)")).select();
	}
	
}
