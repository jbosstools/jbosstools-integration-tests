/******************************************************************************* 
 * Copyright (c) 2020 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.testng.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.swt.condition.ControlIsEnabled;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.list.DefaultList;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.easymport.reddeer.wizard.SmartImportRootWizardPage;
import org.jboss.tools.easymport.reddeer.wizard.SmartImportWizard;
import org.junit.After;

/**
 * 
 * @author Oleksii Korniienko olkornii@redhat.com
 *
 */
public abstract class AbstractTestNGTest {

	public static void checkProblemsView() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		List<Problem> problems = problemsView.getProblems(ProblemType.ERROR);
		assertEquals("There should be no errors in imported project", 0, problems.size());

	}
	
	public static void createProject(String projectName) {
		SmartImportWizard easymportWizard = new SmartImportWizard();
		easymportWizard.open();
		SmartImportRootWizardPage selectImportRootWizardPage = new SmartImportRootWizardPage(easymportWizard);
		File projectFile = new File("resources/" + projectName);
		String path = projectFile.getAbsolutePath();
		selectImportRootWizardPage.selectDirectory(path);
		new WaitUntil(new ControlIsEnabled(new FinishButton(easymportWizard)), TimePeriod.LONG);
		easymportWizard.finish();
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(600));
	}

	public void addLibraries(String projectName) {
		new WorkbenchShell().setFocus();
		new ProjectExplorer().selectProjects(projectName);

		new ContextMenuItem("Properties").select();
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Libraries").activate();
		
		addLibrary("TestNG");
		addLibrary("JUnit");
		addJquery(projectName);
		
		new PushButton("Apply and Close").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	private void addLibrary(String libName) {
		new DefaultTree(1).getItem("Classpath").select();
		new PushButton("Add Library...").click();
		new DefaultList().select(libName);
		new NextButton().click();
		new FinishButton().click();
	}
	
	private void addJquery(String projectName) {
		new DefaultTree(1).getItem("Classpath").select();
		new PushButton("Add JARs...").click();
		new DefaultTreeItem(projectName, "jquery.jar").select();
		new OkButton().click();
	}

	@After
	public void deleteProject() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.deleteAllProjects(true);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
}
