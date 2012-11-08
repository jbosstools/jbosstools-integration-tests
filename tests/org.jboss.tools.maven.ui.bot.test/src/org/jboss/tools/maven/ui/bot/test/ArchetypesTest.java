/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.maven.ui.bot.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;
import org.jboss.tools.maven.ui.bot.test.utils.TableHasRows;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
public class ArchetypesTest extends AbstractMavenSWTBotTest{

	public static final String REPO_URL = "http://repo.maven.apache.org/maven2";
	public static final String NEXUS_URL = "https://repository.jboss.org/nexus/content/repositories/releases/";

	private SWTUtilExt botUtil = new SWTUtilExt(bot);

	@BeforeClass
	public static void setup() throws InterruptedException, CoreException{
		setPerspective("Java");
		updateRepositories();
		
	}

	public static void updateRepositories() throws InterruptedException, CoreException {
		new WorkbenchView("Maven Repositories").open();
		Thread.sleep(5000);
		//new WaitUntil(new TreeCanBeExpanded(new DefaultTreeItem("Global Repositories")),TimePeriod.NORMAL);
		new DefaultTreeItem("Global Repositories","central ("+REPO_URL+")").select();
		new ContextMenu("Rebuild Index").select();
		new WaitUntil(new ShellWithTextIsActive("Rebuild Index"),TimePeriod.NORMAL);
		new PushButton("OK").click();
		SWTUtilExt botUtil = new SWTUtilExt(new SWTBotExt());
		botUtil.waitForAll(Long.MAX_VALUE);
		new DefaultTreeItem("Global Repositories","jboss ("+NEXUS_URL+")").select();
		new ContextMenu("Rebuild Index").select();
		new PushButton("OK").click();
		botUtil.waitForAll(Long.MAX_VALUE);
		new DefaultTreeItem("Global Repositories","jboss ("+NEXUS_URL+")").select();
		new ContextMenu("Update Index").select();
		botUtil.waitForAll(Long.MAX_VALUE);
	}
	
	
	@Test
	public void createSimpleJSFProjectArchetype() throws Exception {
		String projectName = "JsfQuickstart";
		createSimpleMavenProjectArchetype(projectName,"maven-archetype-jsfwebapp", "Nexus Indexer");
		//IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		//assertNoErrors(project);
		assertTrue(isMavenProject(projectName));
		buildProject(projectName, "5 Maven build...", "war",""); //version is 1.0.0
	}
	
	@SuppressWarnings("restriction")
	@Test
	public void createSimpleJarProjectArchetype() throws Exception {
		String projectName = "ArchetypeQuickstart";
		createSimpleMavenProjectArchetype(projectName,"maven-archetype-quickstart", "Internal");
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		assertNoErrors(project);
		assertTrue(isMavenProject(projectName));
		buildProject(projectName, "4 Maven build...", "jar","-0.0.1-SNAPSHOT");
	}
	
	private void createSimpleMavenProjectArchetype(String projectName,String projectType, String catalog) throws InterruptedException,CoreException {
		new ShellMenu("File","Menu","Other...").select();
		new WaitUntil(new ShellWithTextIsActive("New"),TimePeriod.NORMAL);
		new DefaultTreeItem("Maven","Maven Project").select();
		new PushButton("Next >").click();
		new CheckBox("Create a simple project (skip archetype selection)").toggle(true);
		new PushButton("Next >").click();

		Thread.sleep(2000);
		new DefaultCombo(0).setSelection(catalog);
		botUtil.waitForAll();
		new WaitUntil(new TableHasRows(new DefaultTable()),TimePeriod.LONG);
		Thread.sleep(10000);
		new DefaultTable().select(projectType);
		//int index = botExt.table(0).indexOf(projectType, "Artifact Id");
		//if (index == -1) {
		//	fail(projectType + " not found");
		//}
		//shell.table(0).select(index);
		new PushButton("Next >").click();
		new DefaultCombo("Group Id:").setText(projectName);
		new DefaultCombo("Artifact Id:").setText(projectName);
		new PushButton("Finish").click();
		botUtil.waitForAll();
	}
}