/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.hamcrest.core.StringContains;
import org.eclipse.reddeer.common.exception.RedDeerException;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.TreeItemTextMatcher;
import org.eclipse.reddeer.eclipse.core.resources.DefaultProject;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.jdt.ui.preferences.BuildPathsPropertyPage;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.eclipse.reddeer.eclipse.utils.DeleteUtils;
import org.eclipse.reddeer.eclipse.wst.common.project.facet.ui.RuntimesPropertyPage;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.condition.EditorWithTitleIsActive;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.jboss.tools.ws.reddeer.ui.wizards.CreateNewFileWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.jst.j2ee.EARProjectWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.jst.servlet.DynamicWebProjectWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.NewWsdlFileWizard;

/**
 * @author jjankovi
 * @author Radoslav Rabara
 */
public class ProjectHelper {

	private static final Logger LOGGER = Logger.getLogger(ProjectHelper.class.getName());

	private ProjectHelper() {
	};
	
	/**
	 * Method creates basic java class for entered project with entered package
	 * and class name
	 * 
	 * @param projectName
	 * @param pkg
	 * @param cName
	 * @return
	 */
	public static TextEditor createClass(String projectName, String pkg, String className) {
		NewClassCreationWizard wizard = new NewClassCreationWizard();
		wizard.open();

		NewClassWizardPage page = new NewClassWizardPage(wizard);
		page.setPackage(pkg);
		page.setName(className);
		page.setSourceFolder(projectName + "/src");
		wizard.finish();

		return new TextEditor(className + ".java");
	}

	/**
	 * Method creates wsdl file for entered project with entered package name
	 * 
	 * @param projectName
	 * @param wsdlFileName
	 */
	public static DefaultEditor createWsdl(String projectName, String wsdlFileName) {
		EditorHandler.getInstance().closeAll(false);
		NewWsdlFileWizard wizard = new NewWsdlFileWizard();
		wizard.open();

		CreateNewFileWizardPage page = new CreateNewFileWizardPage(wizard);
		page.setFileName(wsdlFileName + ".wsdl");
		page.setParentFolder(projectName + "/src");

		wizard.next();
		wizard.finish();

		new WaitUntil(new EditorWithTitleIsActive(wsdlFileName + ".wsdl"));
		new DefaultCTabItem("Source").activate();

		return new DefaultEditor(wsdlFileName + ".wsdl");
	}

	/**
	 * Method creates new Dynamic Web Project with entered name
	 * 
	 * @param name
	 */
	public static void createProject(String name) {
		DynamicWebProjectWizard wizard = new DynamicWebProjectWizard();
		wizard.open();

		wizard.setProjectName(name);
		wizard.next();
		wizard.next();
		wizard.setGenerateDeploymentDescriptor(true);
		wizard.finish();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.activate();
		projectExplorer.getProject(name).select();
	}

	/**
	 * Method creates new Dynamic Web Project with entered name for ear project
	 * 
	 * @param name
	 */
	public static void createProjectForEAR(String name, String earProject) {
		DynamicWebProjectWizard wizard = new DynamicWebProjectWizard();
		wizard.open();

		wizard.setProjectName(name);
		wizard.addProjectToEar(earProject);
		wizard.finish();

		new WaitWhile(new JobIsRunning());
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.activate();
		projectExplorer.getProject(name).select();
	}

	/**
	 * Method creates new EAR Project with entered name
	 * 
	 * @param name
	 */
	public static void createEARProject(String name) {
		EARProjectWizard wizard = new EARProjectWizard();
		wizard.open();

		new LabeledText("Project name:").setText(name);
		// set EAR version
		DefaultCombo combo = new DefaultCombo(1);
		combo.setSelection(combo.getItems().size() - 1);

		wizard.next();

		new CheckBox("Generate application.xml deployment descriptor").click();

		wizard.finish();
	}

	public static void setProjectJRE(String projectName) {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		PropertyDialog pd = projectExplorer.getProject(projectName).openProperties();
		
		BuildPathsPropertyPage buildPathsPage = new BuildPathsPropertyPage(pd);
		pd.select(buildPathsPage);
		
		buildPathsPage.activateLibrariesTab();
		buildPathsPage.selectLibrary(new TreeItemTextMatcher(StringContains.containsString("JRE System Library")));

		new PushButton(buildPathsPage, IDELabel.Button.EDIT).click();
		Shell editShell = new DefaultShell("Edit Library");
		new RadioButton(editShell, "Alternate JRE:").click();
		new FinishButton(editShell).click();
		new WaitWhile(new ShellIsAvailable(editShell));
		pd.ok();
	}

	public static boolean projectExists(String name) {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		return projectExplorer.containsProject(name);
	}

	public static void deleteAllProjects() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		List<DefaultProject> projects = projectExplorer.getProjects();
		try {
			for (Project project: projects) {
				project.delete(true);
			}
		} catch(RedDeerException e) {
			projectExplorer.close();
			projectExplorer.open();
			projects = projectExplorer.getProjects();
			for (DefaultProject project: projects) {
				try {
					LOGGER.info("Forcing removal of " + project);
					DeleteUtils.forceProjectDeletion(project, true);
				} catch(RuntimeException exception) {
					LOGGER.info("Project " + project.getName() + " was not deleted");
				}
			}
		}
	}

	/**
	 * Cleans All Projects
	 */
	public static void cleanAllProjects() {
		new WaitWhile(new JobIsRunning());
		new ShellMenuItem(IDELabel.Menu.PROJECT, "Clean...").select();
		new DefaultShell("Clean");
		new CheckBox("Clean all projects").toggle(true);
		new PushButton("Clean").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
	}

	public static void importWSTestProject(String projectName, String serverName) {
		try {
			importProject(new File("resources/projects/" + projectName).getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		addConfiguredRuntimeIntoProject(projectName, serverName);
		setProjectJRE(projectName);
	}
	
	/**
	 * Add configured runtime into project as targeted runtime
	 * 
	 * @param project
	 */
	public static void addConfiguredRuntimeIntoProject(String projectName, String configuredRuntime) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		PropertyDialog pd = pe.getProject(projectName).openProperties();
		
		RuntimesPropertyPage rpage = new RuntimesPropertyPage(pd);
		pd.select(rpage);
		new CheckBox(rpage, "Show all runtimes").toggle(false);
		Table runtimes = new DefaultTable(rpage);
		for(TableItem ti : runtimes.getItems()) {
			ti.setChecked(false);
		}
		rpage.selectRuntime(configuredRuntime);
		pd.ok();
	}

	private static void importProject(String projectLocation) {
		ExternalProjectImportWizardDialog importDialog = new ExternalProjectImportWizardDialog();
		importDialog.open();
		WizardProjectsImportPage importPage = new WizardProjectsImportPage(importDialog);
		new WizardProjectsImportPage(importDialog);
		importPage.setRootDirectory(projectLocation);
		assertFalse("There is no project to import", importPage.getProjects().isEmpty());
		importPage.selectAllProjects();
		importPage.copyProjectsIntoWorkspace(true);
		importDialog.finish();
	}
}
