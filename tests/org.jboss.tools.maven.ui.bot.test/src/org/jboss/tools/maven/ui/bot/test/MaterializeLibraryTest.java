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

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tree.ShellTree;
import org.jboss.reddeer.swt.impl.tree.ShellTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.utils.ButtonIsEnabled;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
public class MaterializeLibraryTest extends AbstractMavenSWTBotTest{
	
	private String projectName = "example";
	
	private SWTUtilExt botUtil= new SWTUtilExt(bot);
	
	@BeforeClass
	public static void setup(){
		setPerspective("Java");
		new ShellMenu("Window","Preferences").select();
		new WaitUntil(new ShellWithTextIsActive("Preferences"), TimePeriod.NORMAL);
		new ShellTreeItem("JBoss Tools","Project Examples").select();
		new CheckBox("Show Project Ready wizard").toggle(false);
		new CheckBox("Show readme/cheatsheet file").toggle(false);
		new PushButton("OK").click();
	}
	
	@Test
	public void testMaterializeLibrary() throws Exception{
		new ShellMenu("File","New","Example...").select();
		new ShellTreeItem("JBoss Tools","Project Examples").select();
		new WaitUntil(new ButtonIsEnabled("Next >"), TimePeriod.NORMAL);
		new PushButton("Next >").click();
		new WaitUntil(new ShellWithTextIsActive("New Project Example"), TimePeriod.NORMAL);
		new ShellTreeItem("JBoss Maven Archetypes","Spring MVC Project").select();
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		new DefaultCombo("Project name").setText(projectName);
		new DefaultCombo("Package").setText(projectName);
		new PushButton("Finish").click();
		new WaitUntil(new ShellWithTextIsActive("Importing..."),TimePeriod.VERY_LONG);
		new WaitWhile(new ShellWithTextIsActive("Importing..."),TimePeriod.VERY_LONG);
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		new	ShellTreeItem(projectName,"Maven Dependencies").select();
		new ContextMenu("Materialize Library...").select();
		new WaitUntil(new ShellWithTextIsActive("Materialize Classpath Library"),TimePeriod.NORMAL);
		new PushButton("OK").click();
		bot.sleep(1000);
		new PushButton("OK").click();
		botUtil.waitForAll(Long.MAX_VALUE);
		assertFalse(projectName+" is still a maven project!",isMavenProject(projectName));
		testExcludedResources(projectName);
	}
	
	private void testExcludedResources(String project) throws Exception{
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.selectProject(project);
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+project),TimePeriod.NORMAL);
		new ShellTreeItem("Java Build Path").select();
		bot.tabItem("Source").activate();
		for(TreeItem item: new ShellTree(1).getAllItems()){
			if(item.getText().startsWith("Included")){
				assertTrue("(All) expected in Included patterns",item.getText().endsWith("(All)"));
			} else if (item.getText().startsWith("Excluded")){
				assertTrue("(None) expected in Excluded patterns",item.getText().endsWith("(None)"));
			}
		}
		new PushButton("OK").click();
	}
	
	
}