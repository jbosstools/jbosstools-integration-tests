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
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;
import org.hamcrest.Matcher;
import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.eclipse.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizard;
import org.eclipse.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizardFirstPage;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.api.Tree;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.jst.reddeer.bower.ui.BowerInitDialog;
import org.jboss.tools.jst.reddeer.common.TreeContainsItem;
import org.jboss.tools.jst.reddeer.npm.ui.NpmInitDialog;
import org.jboss.tools.jst.reddeer.tern.ui.TernModulesPropertyPage;
import org.jboss.tools.jst.reddeer.wst.jsdt.ui.wizard.NewJSFileWizardDialog;
import org.jboss.tools.jst.reddeer.wst.jsdt.ui.wizard.NewJSFileWizardPage;
import org.junit.runner.RunWith;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener;
import org.eclipse.debug.core.model.IProcess;

/**
 * TestBase Class for JST tests
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
		JavaProjectWizard jsDialog = new JavaProjectWizard();
		jsDialog.open();
		JavaProjectWizardFirstPage jsPage = new JavaProjectWizardFirstPage(jsDialog);
		jsPage.setName(name);
		jsDialog.finish();
		assertTrue("Project not found", new ProjectExplorer().containsProject(name));
	}

	protected void cleanEditor() {
		cleanEditor(JS_FILE);
	}

	protected static void importExistingProject(String path) {

		ExternalProjectImportWizardDialog importDialog = new ExternalProjectImportWizardDialog();
		importDialog.open();
		WizardProjectsImportPage importPage = new WizardProjectsImportPage(importDialog);
		try {
			importPage.setRootDirectory((new File(path).getCanonicalPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		importPage.copyProjectsIntoWorkspace(true);
		importDialog.finish();
	}

	protected void cleanEditor(String name) {
		DefaultEditor editor = new DefaultEditor(name);
		editor.activate();
		DefaultStyledText text = new DefaultStyledText();
		text.setText("");
		editor.save();
	}

	protected void createJSFile(String filename) {
		NewMenuWizard d = new NewJSFileWizardDialog();
		d.open();
		NewJSFileWizardPage p = new NewJSFileWizardPage(d);
		new LabeledText("Enter or select the parent folder:").setText(PROJECT_NAME);
		p.setFileName(filename);
		d.finish();
		assertTrue(filename + " not found!", new ProjectExplorer().getProject(PROJECT_NAME).containsResource(filename));
	}

	protected void setTernModule(String module) {
		setTernModule(module, PROJECT_NAME);
	}

	protected void setTernModule(String module, String project) {
		PropertyDialog dialog = openProjectProperties(project);
		TernModulesPropertyPage propPage = new TernModulesPropertyPage(dialog);
		dialog.select(propPage);
		new DefaultTable().getItem(module).setChecked(true);
		dialog.ok();
	}

	protected PropertyDialog openProjectProperties() {
		return openProjectProperties(PROJECT_NAME);
	}

	protected PropertyDialog openProjectProperties(String projectName) {
		ProjectExplorer pe = new ProjectExplorer();
		return pe.getProject(projectName).openProperties();
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

	protected static void npmInit(String projectName) {
		NpmInitDialog dialog = new NpmInitDialog();
		dialog.open();
		new LabeledText("Base directory:").setText(BASE_DIRECTORY);
		dialog.finish();
		assertPackageJsonExists();
	}

	protected static void assertPackageJsonExists() {
		File packageJson = new File(BASE_DIRECTORY + "/package.json");
		assertTrue("package.json file does not exist", packageJson.exists());
	}

	protected static void bowerInit() {
		bowerInit(PROJECT_NAME);
	}

	protected static void bowerInit(String projectName) {
		BowerInitDialog dialog = new BowerInitDialog();
		dialog.open();
		new LabeledText("Base directory:").setText(BASE_DIRECTORY);
		dialog.finish();
		assertBowerJsonExists();
	}

	protected static void npmIntall(String projectName) {
		npmInstall(projectName, null);
	}

	@SuppressWarnings("unchecked")
	protected ContextMenuItem runAsNodeJSAppMenu() {
		ContextMenuItem menuItem = new ContextMenuItem(new WithTextMatcher("Run As"),
				new RegexMatcher("(\\d+)( Node.js Application)"));
		return menuItem;
	}

	@SuppressWarnings("unchecked")
	protected ContextMenuItem debugAsNodeJSAppMenu(ProjectItem item) {
		item.select();
		ContextMenuItem menuItem = new ContextMenuItem(new WithTextMatcher("Debug As"),
				new RegexMatcher("(\\d+)( Node.js Application)"));
		return menuItem;
	}

	@SuppressWarnings("unchecked")
	protected static void npmInstall(String projectName, String... dir) {
		new ProjectExplorer().getProject(projectName).select();
		if (dir != null) {
			new ProjectExplorer().getProject(projectName).getProjectItem(dir).select();
		}
		new ContextMenuItem(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( npm Install)")).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	protected static void assertBowerJsonExists() {
		File bowerJson = new File(BASE_DIRECTORY + "/bower.json");
		assertTrue("bower.json file does not exist", bowerJson.exists());
	}

	@SuppressWarnings("unchecked")
	protected static void bowerUpdate(String projectName) {
		new ProjectExplorer().getProject(projectName).select();
		new ContextMenuItem(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Bower Update)")).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	protected static void bowerInstall(String projectName) {
		bowerInstall(projectName, null);
	}

	@SuppressWarnings("unchecked")
	protected static void bowerInstall(String projectName, String... dir) {
		new ProjectExplorer().getProject(projectName).select();
		if (dir != null) {
			new ProjectExplorer().getProject(projectName).getProjectItem(dir).select();
		}
		new ContextMenuItem(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Bower Install)")).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	protected void setLineBreakpoint(TextEditor editor, int lineNumber) throws CoreException {
		editor.activate();
		IResource resource = (IResource) editor.getEditorPart().getEditorInput().getAdapter(IResource.class);
		JavaScriptDebugModel.createLineBreakpoint(resource, lineNumber, -1, -1, new HashMap<String, Object>(), true);
	}

	protected void doubleClickTreeItem(Tree tree, Matcher matcher) {
		List<TreeItem> items = tree.getAllItems();
		for (TreeItem i : items) {
			if (matcher.matches(i.getText())) {
				i.doubleClick();
			}
		}
	}

	protected void resume(Tree tree, Matcher matcher){
		List<TreeItem> items = tree.getAllItems();
		for (TreeItem i : items) {
			if (matcher.matches(i.getText())) {
				i.select();
				new ContextMenuItem("Resume").select();
			}
		}
	}
	
	protected TreeItem getVariable(String name) {

		WorkbenchView variables = new WorkbenchView("Variables");
		variables.activate();
		DefaultTree variablesTree = new DefaultTree();
		
		TreeItem var = null;
		try{
			new WaitUntil(new TreeContainsItem(variablesTree, new WithTextMatcher(name), false));
		}catch (WaitTimeoutExpiredException e) {
			//not found
			return null;
		}
		List<TreeItem> vars = variablesTree.getItems();
		for (TreeItem i : vars) {
			if (i.getText().equals(name)) {
				var = i;
			}
		}
		return var;
	}

	public void terminatePrcs(IProcess[] prcs){
		
		for(IProcess p : prcs){
			try {
				p.terminate();
			} catch (DebugException e) {
				fail(e.getMessage());
			}
			assertTrue("Proces:" + p.getLabel() + " not terminated!", p.isTerminated());
		}
	}
	
	public static boolean portAvailable(int port) {
	    try (Socket ignored = new Socket("localhost", port)) {
	        return false;
	    } catch (IOException ignored) {
	        return true;
	    }
	}
	
	public class NodeJSLaunchListener implements ILaunchesListener{
		
		private ILaunch launch;
		
		public ILaunch getNodeJSLaunch(){
			return launch;
		}

		@Override
		public void launchesAdded(ILaunch[] arg0) {
			this.launch=arg0[0];
		}

		@Override
		public void launchesChanged(ILaunch[] arg0) {
			
		}

		@Override
		public void launchesRemoved(ILaunch[] arg0) {
			
		}

	}
}
