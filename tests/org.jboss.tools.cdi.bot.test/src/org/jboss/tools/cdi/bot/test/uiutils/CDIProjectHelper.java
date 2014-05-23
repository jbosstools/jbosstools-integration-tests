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


package org.jboss.tools.cdi.bot.test.uiutils;

import static org.junit.Assert.*;
import org.eclipse.swt.SWT;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIProjectWizard;
import org.jboss.tools.common.reddeer.label.IDELabel;

public class CDIProjectHelper {
	
	
	/**
	 * Method creates new CDI Project with CDI Web Project wizard
	 * @param projectName
	 */
	public void createCDIProjectWithCDIWizard(String projectName) {
		
		CDIProjectWizard cw = new CDIProjectWizard();
		cw.open();
		WebProjectFirstPage wp = (WebProjectFirstPage)cw.getWizardPage(0);
		wp.setProjectName(projectName);
		cw.finish();
	}
	
	/**
	 * Method creates new CDI Project with Dynamic Web Project, after that it 
	 * adds CDI Support
	 * @param projectName
	 */
	public void createCDIProjectWithDynamicWizard(String projectName) {
		createDynamicWebProject(projectName);
		addCDISupport(projectName);
	}
	
	/**
	 * Method creates new Dynamic Web Project with CDI Preset checked
	 * @param projectName
	 */
	public void createDynamicWebProjectWithCDIPreset(String projectName) {
		WebProjectWizard ww = new WebProjectWizard();
		ww.open();
		WebProjectFirstPage fp = (WebProjectFirstPage)ww.getWizardPage(0);
		fp.setProjectName(projectName);
		fp.setConfiguration("Dynamic Web Project with CDI 1.0 (Context and Dependency Injection)");
		ww.finish();
	}
	
	public boolean projectExists(String projectName){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		return pe.containsProject(projectName);
	}
	
	/**
	 * Method creates new Dynamic Web Project with CDI Facets checked
	 * @param projectName
	 */
	public void createDynamicWebProjectWithCDIFacets(String projectName) {
		WebProjectWizard ww = new WebProjectWizard();
		ww.open();
		WebProjectFirstPage fp = (WebProjectFirstPage)ww.getWizardPage(0);
		fp.setProjectName(projectName);
		fp.activateFacet(CDIConstants.CDI_FACET, null);
		ww.finish();
	}
	
	/**
	 * Set system default jdk in the project
	 * @param projectName
	 */
	/*
	public void addDefaultJDKIntoProject(String projectName) {
		
		projectExplorer.selectProject(projectName);
		bot.menu(IDELabel.Menu.FILE).menu(
				IDELabel.Menu.PROPERTIES).click();
		bot.waitForShell(IDELabel.Shell.PROPERTIES_FOR + " " + projectName);
		SWTBotShell propertiesShell = bot.shell(
				IDELabel.Shell.PROPERTIES_FOR + " " + projectName);
		propertiesShell.activate();
		SWTBotTreeItem item = bot.tree().getTreeItem(
				IDELabel.JavaBuildPathPropertiesEditor.
				JAVA_BUILD_PATH_TREE_ITEM_LABEL);
		item.select();
		bot.tabItem(IDELabel.JavaBuildPathPropertiesEditor.
				LIBRARIES_TAB_LABEL).activate();
		SWTBotTree librariesTree = bot.treeWithLabel(
				"JARs and class folders on the build path:");
		
		for (int i = 0; i < librariesTree.rowCount(); i++) {
			SWTBotTreeItem libraryItem = librariesTree.
					getAllItems()[i];
			if (libraryItem.getText().contains("JRE") || 
				libraryItem.getText().contains("jdk")) {
				libraryItem.select();
				break;
			}
		}
		bot.button(IDELabel.Button.REMOVE).click();
		
		
		bot.button(IDELabel.Button.ADD_LIBRARY).click();
		bot.waitForShell(IDELabel.Shell.ADD_LIBRARY);
		SWTBotShell libraryShell = bot.shell(
				IDELabel.Shell.ADD_LIBRARY);
		libraryShell.activate();
		bot.list().select("JRE System Library");
		bot.button(IDELabel.Button.NEXT).click();
		bot.radio(2).click();
		bot.button(IDELabel.Button.FINISH).click();
		bot.waitWhile(new ShellIsActiveCondition(libraryShell), 
				TaskDuration.LONG.getTimeout());
		bot.button(IDELabel.Button.OK).click();
		bot.waitWhile(new ShellIsActiveCondition(propertiesShell), 
				TaskDuration.LONG.getTimeout());
		util.waitForNonIgnoredJobs();
		
	}
	*/
	/**
	 * Method creates new Dynamic Web Project
	 * @param projectName
	 */
	public void createDynamicWebProject(String projectName) {
		WebProjectWizard ww = new WebProjectWizard();
		ww.open();
		WebProjectFirstPage fp = (WebProjectFirstPage)ww.getWizardPage(0);
		fp.setProjectName(projectName);
		ww.finish();
	}
	
	/**
	 * Method adds CDI support to project with entered name
	 * @param projectName
	 */
	public void addCDISupport(String projectName) {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu(IDELabel.Menu.PACKAGE_EXPLORER_CONFIGURE, 
				CDIConstants.ADD_CDI_SUPPORT).select();
		new DefaultShell("Properties for " + projectName + " (Filtered)");
		new OkButton().click();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
	}
	
	/**
	 * Method adds CDI support to project with entered name with Enter key
	 * @param projectName
	 */
	public void addCDISupportWithEnterKey(String projectName) {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu(IDELabel.Menu.PACKAGE_EXPLORER_CONFIGURE, 
				CDIConstants.ADD_CDI_SUPPORT).select();
		new DefaultShell("Properties for " + projectName + " (Filtered)");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
		try{
			new WaitWhile(new ShellWithTextIsAvailable("Properties for " + projectName + " (Filtered)"));
		} catch (WaitTimeoutExpiredException ex){
			//try Win OS enter
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.LF);
			try{
				new WaitWhile(new ShellWithTextIsAvailable("Properties for " + projectName + " (Filtered)"));
			} catch (WaitTimeoutExpiredException e){
				new CancelButton().click();
				fail("Shell doesnt react on enter key press");
			}
		}
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
	}
	
	/**
	 * Method checks if entered project has CDI support set	
	 * @param projectName
	 * @return
	 */
	public boolean checkCDISupport(String projectName) {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+projectName);
		new DefaultTreeItem(CDIConstants.CDI_PROPERTIES_SETTINGS).select();
		boolean isCDISupported = new CheckBox().isChecked();
		new CancelButton().click();
		return isCDISupported;
	}
	
}
