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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.jboss.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.junit.After;
import org.junit.Test;

public class ProjectWithCDITemplate{
	
	protected String PROJECT_NAME = "CDIProject";
	protected boolean enabledByDefault = true;
	protected String expectedProblem;
	protected List<String> expectedProblemAdded;
	protected String expectedProblemRemoved;
	

	@After
	public void cleanUp() {
		EditorHandler.getInstance().closeAll(false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			try{
				org.jboss.reddeer.direct.project.Project.delete(p.getName(), true, true);
			} catch (Exception ex) {
				AbstractWait.sleep(TimePeriod.DEFAULT);
				if(!p.getTreeItem().isDisposed()){
					org.jboss.reddeer.direct.project.Project.delete(p.getName(), true, true);
				}
			}
		}
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
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
		new WaitUntil(new ProblemExists(ProblemType.ALL), TimePeriod.LONG, false);
		if(expectedProblem != null){
			ProblemsView pw = new ProblemsView();
			pw.open();
			assertEquals(1,pw.getProblems(ProblemType.ALL).size());
			pw.getProblems(ProblemType.WARNING).get(0).getDescription().equals(expectedProblem);
		} else {
			new WaitWhile(new ProblemExists(ProblemType.ALL));
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
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertFalse(cdiSupport.isChecked());
		cdiSupport.toggle(true);
		new OkButton().click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
		new WaitUntil(new ProblemExists(ProblemType.ALL), TimePeriod.LONG, false);
		if(expectedProblemAdded != null){
			ProblemsView pw = new ProblemsView();
			pw.open();
			assertEquals(expectedProblemAdded.size(),pw.getProblems(ProblemType.ALL).size());
			for(Problem p: pw.getProblems(ProblemType.ALL)){
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
			new WaitWhile(new ProblemExists(ProblemType.ALL));
		}
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertTrue(cdiSupport.isChecked());
		new OkButton().click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
	}
	
	@Test
	public void removeCDISupport(){
		openCDIPage();
		LabeledCheckBox cdiSupport = new LabeledCheckBox("CDI support:");
		if(!cdiSupport.isChecked()){
			cdiSupport.toggle(true);
		}
		new OkButton().click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertTrue(cdiSupport.isChecked());
		cdiSupport.toggle(false);
		new OkButton().click();
		new WaitWhile(new ShellIsAvailable("Properties for "+PROJECT_NAME));
		new WaitUntil(new ProblemExists(ProblemType.ALL), TimePeriod.LONG, false);
		if(expectedProblemRemoved != null){
			ProblemsView pw = new ProblemsView();
			pw.open();
			assertEquals(1,pw.getProblems(ProblemType.ALL).size());
			pw.getProblems(ProblemType.WARNING).get(0).getDescription().equals(expectedProblemRemoved);
		} else {
			new WaitWhile(new ProblemExists(ProblemType.ALL));
		}
		openCDIPage();
		cdiSupport = new LabeledCheckBox("CDI support:");
		assertFalse(cdiSupport.isChecked());
		new OkButton().click();
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
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+projectName);
	}

}
