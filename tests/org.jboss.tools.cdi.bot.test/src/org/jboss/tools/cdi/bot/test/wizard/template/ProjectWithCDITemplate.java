/*******************************************************************************
 * Copyright (c) 2011-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.wizard.template;

import static org.junit.Assert.*;

import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.junit.After;
import org.junit.Test;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;

public class ProjectWithCDITemplate{
	
	protected String PROJECT_NAME = "CDIProject";
	protected boolean enabledByDefault = true;
	protected String expectedProblem;
	protected List<String> expectedProblemAdded;
	protected String expectedProblemRemoved;
	

	@After
	public void cleanUp() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.deleteAllProjects();
	}
	
	@Test
	public void testCDISupport(){
		openCDIPage();
		if(enabledByDefault){
			assertTrue(new LabeledCheckBox("CDI support:").isChecked());
		} else {
			assertFalse(new LabeledCheckBox("CDI support:").isChecked());
		}
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+PROJECT_NAME));
		new WaitUntil(new ProblemExists(ProblemType.ANY), TimePeriod.LONG, false);
		if(expectedProblem != null){
			ProblemsView pw = new ProblemsView();
			pw.open();
			assertEquals(1,pw.getProblems(ProblemType.ANY).size());
			pw.getProblems(ProblemType.WARNING).get(0).getDescription().equals(expectedProblem);
		} else {
			new WaitWhile(new ProblemExists(ProblemType.ANY));
		}
	}
	
	@Test
	public void addCDISupport(){
		openCDIPage();
		LabeledCheckBox cdiSupport = new LabeledCheckBox("CDI support:");
		if(cdiSupport.isChecked()){
			cdiSupport.toggle(false);
		}
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+PROJECT_NAME));
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertFalse(cdiSupport.isChecked());
		cdiSupport.toggle(true);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+PROJECT_NAME));
		new WaitUntil(new ProblemExists(ProblemType.ANY), TimePeriod.LONG, false);
		if(expectedProblemAdded != null){
			ProblemsView pw = new ProblemsView();
			pw.open();
			assertEquals(expectedProblemAdded.size(),pw.getProblems(ProblemType.ANY).size());
			for(Problem p: pw.getProblems(ProblemType.ANY)){
				boolean found =false;
				for(String s: expectedProblemAdded){
					if(p.getDescription().contains(s)){
						found = true;
						break;
					}
				}
				if(!found){
					fail(p.getDescription()+" problem was not expected");
				}
			}
		} else {
			new WaitWhile(new ProblemExists(ProblemType.ANY));
		}
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertTrue(cdiSupport.isChecked());
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+PROJECT_NAME));
	}
	
	@Test
	public void removeCDISupport(){
		openCDIPage();
		LabeledCheckBox cdiSupport = new LabeledCheckBox("CDI support:");
		if(!cdiSupport.isChecked()){
			cdiSupport.toggle(true);
		}
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+PROJECT_NAME));
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertTrue(cdiSupport.isChecked());
		cdiSupport.toggle(false);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+PROJECT_NAME));
		new WaitUntil(new ProblemExists(ProblemType.ANY), TimePeriod.LONG, false);
		if(expectedProblemRemoved != null){
			ProblemsView pw = new ProblemsView();
			pw.open();
			assertEquals(1,pw.getProblems(ProblemType.ANY).size());
			pw.getProblems(ProblemType.WARNING).get(0).getDescription().equals(expectedProblemRemoved);
		} else {
			new WaitWhile(new ProblemExists(ProblemType.ANY));
		}
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertFalse(cdiSupport.isChecked());
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+PROJECT_NAME));
	}
	
	private void openCDIPage(){
		openProjectProperties(PROJECT_NAME);
		new DefaultTreeItem("CDI (Contexts and Dependency Injection) Settings").select();
	}
	
	private void openProjectProperties(String projectName){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(projectName);
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+projectName);
	}

}
