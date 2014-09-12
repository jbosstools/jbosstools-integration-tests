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

import static org.junit.Assert.assertThat;

import org.eclipse.jdt.internal.ui.text.folding.DefaultJavaFoldingPreferenceBlock;
import org.hamcrest.core.Is;
import org.hamcrest.core.StringContains;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
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

	private final ProjectExplorer projectExplorer = new ProjectExplorer();

	/**
	 * Method creates basic java class for entered project with 
	 * entered package and class name
	 * @param projectName
	 * @param pkg
	 * @param cName
	 * @return
	 */
	public TextEditor createClass(String projectName, String pkg, String className) {
		NewJavaClassWizardDialog wizard = new NewJavaClassWizardDialog();
		wizard.open();

		NewJavaClassWizardPage page = wizard.getFirstPage();
		page.setPackage(pkg);
		page.setName(className);
		page.setSourceFolder(projectName + "/src");
		wizard.finish();

		return new TextEditor(className + ".java");
	}

	/**
	 * Method creates wsdl file for entered project with 
	 * entered package name
	 * @param projectName
	 * @param wsdlFileName
	 */
	public DefaultEditor createWsdl(String projectName, String wsdlFileName) {
		NewWsdlFileWizard wizard = new NewWsdlFileWizard();
		wizard.open();

		CreateNewFileWizardPage page = new CreateNewFileWizardPage();
		page.setFileName(wsdlFileName + ".wsdl");
		page.setParentFolder(projectName + "/src");

		wizard.next();
		wizard.finish();

		new DefaultCTabItem("Source").activate();
		
		return new DefaultEditor(wsdlFileName + ".wsdl");
	}
	
	/**
	 * Method creates new Dynamic Web Project with entered name
	 * @param name
	 */
	public void createProject(String name) {
		DynamicWebProjectWizard wizard = new DynamicWebProjectWizard();
		wizard.open();

		wizard.setProjectName(name);
		wizard.next();
		wizard.next();
		wizard.setGenerateDeploymentDescriptor(true);
		wizard.finish();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		projectExplorer.getProject(name).select();
	}
	
	/**
	 * Method creates new Dynamic Web Project with entered name for
	 * ear project
	 * @param name
	 */
	public void createProjectForEAR(String name, String earProject) {
		DynamicWebProjectWizard wizard = new DynamicWebProjectWizard();
		wizard.open();

		wizard.setProjectName(name);
		wizard.addProjectToEar(earProject);
		wizard.finish();

		new WaitWhile(new JobIsRunning());
		projectExplorer.getProject(name).select();
	}

	/**
	 * Method creates new EAR Project with entered name
	 * @param name
	 */
	public void createEARProject(String name) {
		EARProjectWizard wizard = new EARProjectWizard();
		wizard.open();

		new LabeledText("Project name:")
			.setText(name);
		// set EAR version
		DefaultCombo combo = new DefaultCombo(1);
		combo.setSelection(combo.getItems().size()-1);

		wizard.next();

		new CheckBox("Generate application.xml deployment descriptor").click();

		wizard.finish();
	}

	/**
	 * Method generates Deployment Descriptor for entered project 
	 * @param projectName
	 */
	public void createDD(String projectName) {
		Project project = getProject(projectName);
		expandProject(project.getTreeItem());
		String dd = "Deployment Descriptor: " + projectName;
		project.getProjectItem(dd).select();

        new ContextMenu("Generate Deployment Descriptor Stub").select();

        new WaitWhile(new JobIsRunning());
    }

	private void expandProject(TreeItem projectItem) {
		do {
			projectItem.collapse();
			projectItem.expand();
		} while (!projectItem.isExpanded());
	}

	/**
	 * Add configured runtime into project as targeted runtime
	 * @param project
	 */
	public void addConfiguredRuntimeIntoProject(String projectName, 
			String configuredRuntime) {
		PropertiesDialog dialog = new PropertiesDialog();
		dialog.open(projectName);

		TargetedRuntimesPropertiesPage page = new TargetedRuntimesPropertiesPage();
		page.select();
		page.setSelectAllRuntimes(true);
		page.checkAllRuntimes(false);
		page.checkRuntime(configuredRuntime, true);

		dialog.finish();
	}

	public void setProjectJRE(String projectName) {
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

	private Project getProject(String projectName) {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		Project project =  projectExplorer.getProject(projectName);
		assertThat("Project name", project.getName(), Is.is(projectName));
		return project;
	}
}
