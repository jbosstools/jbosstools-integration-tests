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

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Before;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
@OpenPerspective(JavaPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT)
public class MaterializeLibraryTest extends AbstractMavenSWTBotTest{
	
	private String projectName = "example";
	
	@Before
	public void setup(){
		WorkbenchPreferenceDialog wp = new WorkbenchPreferenceDialog();
		wp.open();
		wp.select("JBoss Tools","Project Examples");
		new CheckBox("Show Project Ready wizard").toggle(false);
		new CheckBox("Show readme/cheatsheet file").toggle(false);
		new CheckBox("Show Quick Fix dialog").toggle(false);
		wp.ok();
	}
	
	@Test
	public void testMaterializeLibrary() throws Exception{
		new ShellMenuItem("File","New","Example...").select();
		new DefaultTreeItem("JBoss Tools","Project Examples").select();
		new PushButton("Next >").click();
		new DefaultShell("New Project Example");
		new DefaultTreeItem("JBoss Maven Archetypes","Spring MVC Project").select();
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		new LabeledCombo("Project name").setText(projectName);
		new LabeledCombo("Package").setText(projectName);
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsAvailable("New Project Example"), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		new	DefaultTreeItem(projectName,"Maven Dependencies").select();
		new ContextMenuItem("Copy Classpath Libraries...").select();
		new WaitUntil(new ShellIsAvailable("Copy Classpath Libraries"),TimePeriod.DEFAULT);
		new PushButton("OK").click();
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable("Materialize Classpath Library"), TimePeriod.DEFAULT);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		testExcludedResources(projectName);
	}
	
	private void testExcludedResources(String project) throws Exception{
		PropertyDialog pd = openPropertiesPackage(project);
		new WaitUntil(new ShellIsAvailable("Properties for "+project),TimePeriod.DEFAULT);
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Source").activate();
		for(TreeItem item: new DefaultTree(1).getAllItems()){
			if(item.getText().startsWith("Included")){
				assertTrue("(All) expected in Included patterns",item.getText().endsWith("(All)"));
			} else if (item.getText().startsWith("Excluded")){
				assertTrue("(None) expected in Excluded patterns",item.getText().endsWith("(None)"));
			}
		}
		pd.ok();
		new WaitWhile(new ShellIsAvailable("Properties for "+project),TimePeriod.DEFAULT);
		deleteProjects(true);
	}
	
	
}