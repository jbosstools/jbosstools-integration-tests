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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.Before;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
public class MaterializeLibraryTest extends AbstractMavenSWTBotTest{
	
	private String projectName = "example";
	
	@Before
	public void setup(){
		setPerspective("Java");
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
		new WaitUntil(new WidgetIsEnabled(new PushButton("Next >")), TimePeriod.NORMAL);
		new PushButton("Next >").click();
		new WaitUntil(new ShellWithTextIsActive("New Project Example"), TimePeriod.NORMAL);
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
		assertFalse(projectName+" is still a maven project!",isMavenProject(projectName));
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
		deleteProjects(true,false);
	}
	
	
}