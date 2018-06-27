/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
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

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.condition.ProblemExists;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectFirstPage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.LabeledCheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenu;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIProjectWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.facet.CDIInstallWizardPage;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.junit.After;
import org.junit.Test;

public class CDIWebProjectWizardTemplate{
	
	
	private static final String PROJECT_NAME = "CDIProject";
	protected String CDIVersion;
	
	@InjectRequirement
	protected ServerRequirement sr;
	
	@After
	public void cleanup(){
		EditorHandler.getInstance().closeAll(false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			try{
				org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
			} catch (Exception ex) {
				AbstractWait.sleep(TimePeriod.DEFAULT);
				if(!p.getTreeItem().isDisposed()){
					org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
				}
			}
		}
	}
	
	@Test
	public void createCDIProject(){
		CDIProjectWizard cw = new CDIProjectWizard();
		cw.open();
		WebProjectFirstPage fp = new WebProjectFirstPage(cw);
		fp.setProjectName(PROJECT_NAME);
		assertEquals(sr.getRuntimeName(),fp.getTargetRuntime());
		assertEquals("Dynamic Web Project with CDI "+CDIVersion+" (Contexts and Dependency Injection)",fp.getConfiguration());
		fp.activateFacet("1.8", "Java");
		cw.finish();
		isCDISupportEnabled(PROJECT_NAME);
		isCDIFacetEnabled(PROJECT_NAME, CDIVersion);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		assertTrue(pe.containsProject(PROJECT_NAME));
		assertTrue(pe.getProject(PROJECT_NAME).containsResource("WebContent","WEB-INF","beans.xml"));
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").open();
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateSourcePage();
		assertEquals(0,beans.getMarkers().size());
		new WaitUntil(new ProblemExists(ProblemType.ALL), TimePeriod.LONG, false);
		new WaitWhile(new ProblemExists(ProblemType.ALL));
	}
	
	//cdi1.1+
	//cd1.0 should show warning
	@Test
	public void createCDIProjectWithoutBeansXml(){
		CDIProjectWizard cw = new CDIProjectWizard();
		cw.open();
		WebProjectFirstPage fp = new WebProjectFirstPage(cw);
		fp.setProjectName(PROJECT_NAME);
		assertEquals(sr.getRuntimeName(),fp.getTargetRuntime());
		assertEquals("Dynamic Web Project with CDI "+CDIVersion+" (Contexts and Dependency Injection)",fp.getConfiguration());
		fp.activateFacet("1.8", "Java");
		cw.next();
		cw.next();
		cw.next();
		CDIInstallWizardPage ip = new CDIInstallWizardPage(cw);
		ip.toggleCreateBeansXml(false);
		cw.finish();
		isCDISupportEnabled(PROJECT_NAME);
		isCDIFacetEnabled(PROJECT_NAME, CDIVersion);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		assertTrue(pe.containsProject(PROJECT_NAME));
		assertFalse(pe.getProject(PROJECT_NAME).containsResource("WebContent","WEB-INF","beans.xml"));
		new WaitUntil(new ProblemExists(ProblemType.ALL), TimePeriod.LONG, false);
		if (CDIVersion.equals("1.0")) {
			assertTrue(new ProblemExists(ProblemType.ALL).test());	
		} else {	
			new WaitWhile(new ProblemExists(ProblemType.ALL));	
		}
	}
	
	//dynamic web..and other JEE7 project-enabled
	
	private boolean isCDISupportEnabled(String projectName){
		openProjectProperties(projectName);
		new DefaultTreeItem("CDI (Contexts and Dependency Injection) Settings").select();
		boolean toReturn = new LabeledCheckBox("CDI support:").isChecked();
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellIsAvailable("Properties for "+projectName));
		return toReturn;
	}
	
	private boolean isCDIFacetEnabled(String projectName, String cdiVersion){
		openProjectProperties(projectName);
		new DefaultTreeItem("Project Facets").select();
		boolean result = new DefaultTreeItem(new DefaultTree(1),"CDI (Contexts and Dependency Injection)").isChecked();
		result = result && new DefaultTreeItem(new DefaultTree(1),"CDI (Contexts and Dependency Injection)").getCell(1).equals(cdiVersion);
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellIsAvailable("Properties for "+projectName), TimePeriod.DEFAULT);
		return result;
	}
	
	private void openProjectProperties(String projectName){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(projectName);
		new ContextMenu().getItem("Properties").select();
		new DefaultShell("Properties for "+projectName);
	}
	
}
