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
package org.jboss.tools.maven.ui.bot.test.conversion;

import static org.junit.Assert.assertTrue;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Before;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
@CleanWorkspace
@OpenPerspective(JavaPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class MaterializeLibraryTest extends AbstractMavenSWTBotTest{
	
	private String projectName = "example";
	
	@Before
	public void setup(){
		new ShellMenu("Window","Preferences").select();
		new WaitUntil(new ShellWithTextIsActive("Preferences"), TimePeriod.NORMAL);
		new DefaultTreeItem("JBoss Tools","Project Examples").select();
		new CheckBox("Show Project Ready wizard").toggle(false);
		new CheckBox("Show readme/cheatsheet file").toggle(false);
		new CheckBox("Show Quick Fix dialog").toggle(false);
		new PushButton("OK").click();
	}
	
	@Test
	public void testMaterializeLibrary() throws Exception{
		new ShellMenu("File","New","Example...").select();
		new DefaultTreeItem("JBoss Tools","Project Examples").select();
		new PushButton("Next >").click();
		new DefaultShell("New Project Example");
		new DefaultTreeItem("JBoss Maven Archetypes","Spring MVC Project").select();
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		new LabeledCombo("Project name").setText(projectName);
		new LabeledCombo("Package").setText(projectName);
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("New Project Example"), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		new	DefaultTreeItem(projectName,"Maven Dependencies").select();
		new ContextMenu("Copy Classpath Libraries...").select();
		new WaitUntil(new ShellWithTextIsActive("Copy Classpath Libraries"),TimePeriod.NORMAL);
		new PushButton("OK").click();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Materialize Classpath Library"), TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		testExcludedResources(projectName);
	}
	
	private void testExcludedResources(String project) throws Exception{
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(project).select();
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+project),TimePeriod.NORMAL);
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Source").activate();
		for(TreeItem item: new DefaultTree(1).getAllItems()){
			if(item.getText().startsWith("Included")){
				assertTrue("(All) expected in Included patterns",item.getText().endsWith("(All)"));
			} else if (item.getText().startsWith("Excluded")){
				assertTrue("(None) expected in Excluded patterns",item.getText().endsWith("(None)"));
			}
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Properties for "+project),TimePeriod.NORMAL);
		deleteProjects(true);
	}
	
	
}