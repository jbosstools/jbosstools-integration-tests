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

import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.m2e.core.ui.wizard.MavenProjectWizard;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
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
		openProjectWizard();

		new LabeledCombo("Group Id:").setText(projectName);
		new LabeledCombo("Artifact Id:").setText(projectName);

		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	public static void openProjectWizard() {
		MavenProjectWizard MPW = new MavenProjectWizard();
		MPW.open();
		MPW.next();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		new LabeledText("Filter:").setText("quarkus-amazon-lambda-archetype");
		new DefaultTable(0).select(0);
		MPW.next();
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
