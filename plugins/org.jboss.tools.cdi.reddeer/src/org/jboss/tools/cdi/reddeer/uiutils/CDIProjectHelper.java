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


package org.jboss.tools.cdi.reddeer.uiutils;

import static org.junit.Assert.*;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
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
	public void createCDIProjectWithCDIWizard(String projectName, String runtime) {
		
		CDIProjectWizard cw = new CDIProjectWizard();
		cw.open();
		WebProjectFirstPage wp = (WebProjectFirstPage)cw.getWizardPage(0);
		wp.setProjectName(projectName);
		wp.setTargetRuntime(runtime);
		cw.finish();
	}
	
	/**
	 * Method creates new CDI 1.1 Project with CDI Web Project wizard
	 * @param projectName
	 * @param targetRuntime
	 */
	public void createCDI11ProjectWithCDIWizard(String projectName, String targetRuntime) {
		
		CDIProjectWizard cw = new CDIProjectWizard();
		cw.open();
		WebProjectFirstPage wp = (WebProjectFirstPage)cw.getWizardPage(0);
		wp.setProjectName(projectName);
		wp.setTargetRuntime(targetRuntime);
		wp.setConfiguration("Dynamic Web Project with CDI 1.1 (Context and Dependency Injection)");
		cw.finish();
	}
	
	/**
	 * Method creates new CDI Project with Dynamic Web Project, after that it 
	 * adds CDI Support
	 * @param projectName
	 */
	public void createCDIProjectWithDynamicWizard(String projectName, String runtime) {
		createDynamicWebProject(projectName, runtime);
		addCDISupport(projectName);
	}
	
	/**
	 * Method creates new Dynamic Web Project with CDI Preset checked
	 * @param projectName
	 */
	public void createDynamicWebProjectWithCDIPreset(String targetRuntime, String projectName) {
		WebProjectWizard ww = new WebProjectWizard();
		ww.open();
		WebProjectFirstPage fp = (WebProjectFirstPage)ww.getWizardPage(0);
		fp.setProjectName(projectName);
		fp.setTargetRuntime(targetRuntime);
		fp.setConfiguration("Dynamic Web Project with CDI 1.0 (Context and Dependency Injection)");
		ww.finish();
	}
	
	public boolean projectExists(String projectName){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		return pe.containsProject(projectName);
	}
	
	/**
	 * Method creates new Dynamic Web Project with CDI Facets checked
	 * @param projectName
	 */
	public void createDynamicWebProjectWithCDIFacets(String projectName, String runtime) {
		WebProjectWizard ww = new WebProjectWizard();
		ww.open();
		WebProjectFirstPage fp = (WebProjectFirstPage)ww.getWizardPage(0);
		fp.setProjectName(projectName);
		fp.setTargetRuntime(runtime);
		fp.activateFacet(CDIConstants.CDI_FACET, null);
		ww.finish();
	}
	
	/**
	 * Method creates new Dynamic Web Project
	 * @param projectName
	 */
	public void createDynamicWebProject(String projectName, String runtime) {
		WebProjectWizard ww = new WebProjectWizard();
		ww.open();
		WebProjectFirstPage fp = (WebProjectFirstPage)ww.getWizardPage(0);
		fp.setProjectName(projectName);
		fp.setTargetRuntime(runtime);
		ww.finish();
	}
	
	/**
	 * Method adds CDI support to project with entered name
	 * @param projectName
	 */
	public void addCDISupport(String projectName) {
		ProjectExplorer pe = new ProjectExplorer();
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
		ProjectExplorer pe = new ProjectExplorer();
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
		ProjectExplorer pe = new ProjectExplorer();
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
