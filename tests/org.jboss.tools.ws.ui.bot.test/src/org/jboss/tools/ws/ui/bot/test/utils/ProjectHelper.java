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
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.condition.EditorWithTitleIsActive;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.jboss.tools.ws.reddeer.ui.wizards.CreateNewFileWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.jst.j2ee.EARProjectWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.jst.servlet.DynamicWebProjectWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.NewWsdlFileWizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.JavaBuildPathPropertiesPage;
import org.jboss.tools.ws.ui.bot.test.uiutils.PropertiesDialog;
import org.jboss.tools.ws.ui.bot.test.uiutils.TargetedRuntimesPropertiesPage;

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
		NewJavaClassWizardDialog wizard = new NewJavaClassWizardDialog();
		wizard.open();

		NewJavaClassWizardPage page = new NewJavaClassWizardPage();
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

		CreateNewFileWizardPage page = new CreateNewFileWizardPage();
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
		PropertiesDialog dialog = new PropertiesDialog();
		dialog.open(projectName);

		JavaBuildPathPropertiesPage page = new JavaBuildPathPropertiesPage();
		page.select();
		page.activateLibrariesTab();
		page.selectLibrary(StringContains.containsString("JRE System Library"));

		new PushButton(IDELabel.Button.EDIT).click();
		new DefaultShell("Edit Library");
		new RadioButton("Alternate JRE:").click();
		new PushButton(IDELabel.Button.FINISH).click();

		dialog.finish();
	}

	public static boolean projectExists(String name) {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		return projectExplorer.containsProject(name);
	}

	public static void deleteAllProjects() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		List<Project> projects = projectExplorer.getProjects();
		try {
			for (Project project: projects) {
				project.delete(true);
			}
		} catch(RedDeerException e) {
			projectExplorer.close();
			projectExplorer.open();
			projects = projectExplorer.getProjects();
			for (Project project: projects) {
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
		new ShellMenu(IDELabel.Menu.PROJECT, "Clean...").select();
		new DefaultShell("Clean");
		new RadioButton("Clean all projects").click();
		new PushButton(IDELabel.Button.OK).click();
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
		PropertiesDialog dialog = new PropertiesDialog();
		dialog.open(projectName);

		TargetedRuntimesPropertiesPage page = new TargetedRuntimesPropertiesPage();
		page.select();
		page.setSelectAllRuntimes(true);
		page.checkAllRuntimes(false);
		page.checkRuntime(configuredRuntime, true);

		dialog.finish(TimePeriod.LONG);
	}

	private static void importProject(String projectLocation) {
		ExternalProjectImportWizardDialog importDialog = new ExternalProjectImportWizardDialog();
		importDialog.open();
		WizardProjectsImportPage importPage = new WizardProjectsImportPage();
		importPage.setRootDirectory(projectLocation);
		assertFalse("There is no project to import", importPage.getProjects().isEmpty());
		importPage.selectAllProjects();
		importPage.copyProjectsIntoWorkspace(true);
		importDialog.finish();
	}
}
