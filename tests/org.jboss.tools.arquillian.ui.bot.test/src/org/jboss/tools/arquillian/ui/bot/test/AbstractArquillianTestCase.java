/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation    
 ******************************************************************************/

package org.jboss.tools.arquillian.ui.bot.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.m2e.core.ui.wizard.MavenProjectWizardPage;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.arquillian.ui.bot.reddeer.junit.ArquillianJUnitTestCaseWizard;
import org.jboss.tools.arquillian.ui.bot.reddeer.junit.JUnitTestCaseWizardPage;
import org.jboss.tools.arquillian.ui.bot.reddeer.maven.UpdateMavenProjectDialog;
import org.jboss.tools.arquillian.ui.bot.reddeer.profile.AddArquillianProfilesDialog;
import org.jboss.tools.arquillian.ui.bot.reddeer.support.AddArquillianSupportDialog;
import org.jboss.tools.maven.reddeer.profiles.SelectProfilesDialog;
import org.jboss.tools.maven.reddeer.wizards.MavenProjectWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenProjectWizardThirdPage;


/**
 * Contains common methods for Arquillian tests. 
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class AbstractArquillianTestCase {

	protected static final String PROJECT_NAME = "arquillian-test-project";
	
	public static final String PROFILE_NAME = "WILDFLY_REMOTE_8.X";
	
	protected static final String PACKAGE = "org.jboss.tools.arquillian.ui.test";
	
	protected static final String TEST_CASE = "ArquillianTest";
	
	protected static final String SOURCE_FOLDER = PROJECT_NAME + "/src/test/java";
	
	private static final Logger log = Logger.getLogger(AbstractArquillianTestCase.class);
	
	protected Project getProject() {
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		Project project = explorer.getProject(PROJECT_NAME);
		return project;
	}
	
	@SuppressWarnings("rawtypes")
	protected void checkProblems() {
		log.step("Check errors in Problems view");
		ProblemsView view = new ProblemsView();
		view.open();
		
		List<Problem> problems = view.getProblems(ProblemType.ERROR);
		assertThat("There are errors", problems, is((List) new ArrayList<Problem>()));
	}
	
	@org.junit.After
	public void after(){
		deleteProject();
	}
	
	private void deleteProject() {
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		Project project = explorer.getProject(PROJECT_NAME);
		project.delete(true);
	}

	protected void prepareProject(){
		setupProject();
		addArquillianSupport();
		forceMavenRepositoryUpdate();
	}
	
	protected void forceMavenRepositoryUpdate() {
		log.step("Force Maven update snapshots/releases");
		UpdateMavenProjectDialog dialog = new UpdateMavenProjectDialog();
		dialog.open(getProject());
		new CheckBox("Offline").toggle(false);
		dialog.forceUpdate(true);
		dialog.ok();
		new WaitWhile(new JobIsRunning());
	}
	
	protected void setupProject(){
		log.step("Create maven project with name " + PROJECT_NAME);
		MavenProjectWizard wizard = new MavenProjectWizard();
		wizard.open();
		
		// Check "Create a simple project"		
		MavenProjectWizardPage mp = new MavenProjectWizardPage();
		mp.createSimpleProject(true);

		wizard.next();
		
		MavenProjectWizardThirdPage thirdPage = new MavenProjectWizardThirdPage();
		thirdPage.setGAV(PROJECT_NAME, PROJECT_NAME, null);
		
		wizard.finish();
		
		forceMavenRepositoryUpdate();
	}
	
	protected void addArquillianSupport() {
		log.step("Add Arquillian support");
		Project project = getProject();
		
		AddArquillianSupportDialog dialog = new AddArquillianSupportDialog();
		dialog.open(project);
		dialog.ok();	
	}
	
	protected void addArquillianProfile(){
		log.step("Add Arquillian profile");
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		Project project = explorer.getProject(PROJECT_NAME);
		
		AddArquillianProfilesDialog dialog = new AddArquillianProfilesDialog();
		dialog.open(project);
		dialog.selectProfile(PROFILE_NAME);
		dialog.ok();
		new WaitWhile(new JobIsRunning());
	}
	
	protected void selectMavenProfile() {
		log.step("Select maven profile");
		refreshProject();
		SelectProfilesDialog dialog = new SelectProfilesDialog(PROJECT_NAME);
		dialog.open();
		new WaitWhile(new JobIsRunning());
		dialog.activateProfile(PROFILE_NAME);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
	}
	
	protected void createTestCase(){
		ArquillianJUnitTestCaseWizard wizard = new ArquillianJUnitTestCaseWizard();
		wizard.open();
		
		JUnitTestCaseWizardPage page = new JUnitTestCaseWizardPage();
		page.setSourceFolder(SOURCE_FOLDER);
		page.setPackage(PACKAGE);
		page.setName(TEST_CASE);
		
		wizard.finish();
	}
	
	
	protected void changeContent() {
		TextEditor editor = new TextEditor(TEST_CASE + ".java");
		editor.selectText("fail(\"Not yet implemented\");");
		KeyboardFactory.getKeyboard().type(SWT.DEL);
		editor.save();
	}
	
	protected void refreshProject (){
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		Project project = explorer.getProject(PROJECT_NAME);
		project.refresh();
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
	}
}
