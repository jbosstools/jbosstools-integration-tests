/*******************************************************************************
 * Copyright (c) 2011-2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.wizard.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.condition.ProblemExists;
import org.eclipse.reddeer.eclipse.jst.ejb.ui.project.facet.EjbProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.ejb.ui.project.facet.EjbProjectWizard;
import org.eclipse.reddeer.eclipse.jst.j2ee.ui.project.facet.UtilityProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.j2ee.ui.project.facet.UtilityProjectWizard;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectWizard;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.eclipse.wst.web.ui.wizards.DataModelFacetCreationWizardPage;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.LabeledCheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenu;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.hamcrest.core.StringContains;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.condition.SpecificProblemExists;
import org.junit.After;
import org.junit.Test;

@CleanWorkspace
public class ProjectWithCDITemplate extends CDITestBase {
	
	protected boolean enabledByDefault = true;
	protected String ignoredProblem;
	protected String expectedProblemAdded;

	@After
	public void cleanUpTheWorkspace() {
		cleanUp();
	}
	
	@Test
	public void testCDISupport(){
		openCDIPage();
		if(enabledByDefault){
			assertTrue(new LabeledCheckBox("CDI support:").isChecked());
		} else {
			assertFalse(new LabeledCheckBox("CDI support:").isChecked());
		}
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
		new WaitUntil(new ProblemExists(ProblemType.ALL), false);
		
		if(enabledByDefault && expectedProblemAdded != null){
			assertCdiProblemFound();
		} else {
			assertNoProblemFound();
		}
		new WaitWhile(new ProblemExists(ProblemType.ALL), false);	
	}
	
	@Test
	public void addCDISupport(){
		openCDIPage();
		LabeledCheckBox cdiSupport = new LabeledCheckBox("CDI support:");
		if(cdiSupport.isChecked()){
			cdiSupport.toggle(false);
		}
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
		
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertFalse(cdiSupport.isChecked());
		cdiSupport.toggle(true);
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
		
		if(expectedProblemAdded != null){
			assertCdiProblemFound();
		} else {
			assertNoProblemFound();
		}
		
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertTrue(cdiSupport.isChecked());
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
	}
	
	@Test
	public void removeCDISupport(){
		openCDIPage();
		LabeledCheckBox cdiSupport = new LabeledCheckBox("CDI support:");
		if(!cdiSupport.isChecked()){
			cdiSupport.toggle(true);
		}
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
		
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertTrue(cdiSupport.isChecked());
		cdiSupport.toggle(false);
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
		new WaitWhile(new SpecificProblemExists(new StringContains(expectedProblemAdded)));
		
		assertNoProblemFound();
		
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertFalse(cdiSupport.isChecked());
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
	}
	
	private void openCDIPage(){
		openProjectProperties(PROJECT_NAME);
		new DefaultTreeItem("CDI (Contexts and Dependency Injection) Settings").select();
	}
	
	private void openProjectProperties(String projectName){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(projectName);
		new ContextMenu().getItem("Properties").select();
		new DefaultShell("Properties for "+projectName);
	}
	
	private List<Problem> removeIgnoredProblem(List<Problem> problems){
		if (ignoredProblem == null) {
			return problems;
		}
		Iterator<Problem> iterator = problems.iterator();
		
		while(iterator.hasNext()) {
			if (iterator.next().getDescription().contains(ignoredProblem)) {
				iterator.remove();
				break;
			}
		}
		return problems;
	}
	
	private void assertCdiProblemFound() {
		try {
			new WaitUntil(new SpecificProblemExists(new StringContains(expectedProblemAdded)), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Problem '" + expectedProblemAdded + "' not found.");
		}
		ProblemsView pw = new ProblemsView();
		pw.open();
		List<Problem> problems = pw.getProblems(ProblemType.ALL);
		assertEquals("Unexpected problem found. Problems: " + problems, 1, removeIgnoredProblem(problems).size());
	}
	
	private void assertNoProblemFound() {
		new WaitWhile(new ProblemExists(ProblemType.ALL), false);
		
		ProblemsView pw = new ProblemsView();
		pw.open();
		List<Problem> problems = removeIgnoredProblem(pw.getProblems(ProblemType.ALL));
		
		assertEquals("Unexpected problem found: " + problems, 0, problems.size());
	}
	
	public void createUtilityProject() {
		NewMenuWizard pw = new UtilityProjectWizard();
		DataModelFacetCreationWizardPage fp = new UtilityProjectFirstPage(pw);
		createProject(pw, fp);
	}

	public void createDynamicWebProject() {
		NewMenuWizard pw = new WebProjectWizard();
		DataModelFacetCreationWizardPage fp = new WebProjectFirstPage(pw);
		createProject(pw, fp);
	}
	
	public void createEjbProject() {
		NewMenuWizard pw = new EjbProjectWizard();
		DataModelFacetCreationWizardPage fp = new EjbProjectFirstPage(pw);
		createProject(pw, fp);
	}

}
