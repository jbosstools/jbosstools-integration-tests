/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIProjectWizard;
import org.jboss.tools.cdi.reddeer.uiutils.BeansHelper;
import org.jboss.tools.cdi.reddeer.uiutils.BeansXMLHelper;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.jboss.tools.cdi.reddeer.uiutils.OpenOnHelper;
import org.jboss.tools.cdi.reddeer.uiutils.ProjectHelper;
import org.jboss.tools.cdi.reddeer.uiutils.ValidationHelper;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage;
import org.junit.After;
import org.junit.Before;

public class CDITestBase{
	
	protected static String PROJECT_NAME = "CDIProject";
	protected static final String PACKAGE_NAME = "cdi";
	
	protected static final Logger LOGGER = Logger.getLogger(CDITestBase.class.getName());
	protected static final BeansXMLHelper beansXMLHelper = new BeansXMLHelper();
	protected static final BeansHelper beansHelper = new BeansHelper();
	protected static final EditorResourceHelper editResourceUtil = new EditorResourceHelper();
	protected static final ValidationHelper validationHelper = new ValidationHelper();
	protected static final OpenOnHelper openOnHelper = new OpenOnHelper();
	protected static final ProjectHelper projectHelper = new ProjectHelper();
	
	@InjectRequirement
    private ServerRequirement sr;

	@Before
	public void prepareWorkspace() {
		if (!projectHelper.projectExists(PROJECT_NAME)) {
			CDIProjectWizard cw = new CDIProjectWizard();
			cw.open();
			WebProjectFirstPage fp  = new WebProjectFirstPage();
			fp.setProjectName(PROJECT_NAME);
			fp.setTargetRuntime(sr.getRuntimeNameLabelText(sr.getConfig()));
			cw.finish();
			new WaitUntil(new JobIsRunning(),TimePeriod.NORMAL, false);
			new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		}
	}
	
	protected void deleteAllProjects(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		try{
			pe.deleteAllProjects(true);
		} catch (SWTLayerException ex){
			for (Project project : pe.getProjects()){
				DeleteUtils.forceProjectDeletion(project, true);
			}
		}
	}
	
	protected static String readFile(String path) {
		Scanner s = null;
		try {
			s = new Scanner(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			fail("unable to find file "+path);
		}
		Scanner s1 = s.useDelimiter("\\A");
		String file =  s.next();
		s.close();
		s1.close();
		return file;
		
	}
	
	@After
	public void waitForJobs() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
		
	protected String getProjectName() {
		return PROJECT_NAME;
	}
	
	protected String getPackageName() {
		return PACKAGE_NAME;
	}
	
	protected static void disableSourceLookup() {
		// wait for some shell to get activated
		ShellLookup.getInstance().getActiveShell();
		String originalShellText = new DefaultShell().getText();
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		SourceLookupPreferencePage sourceLookupPreferencePage = new SourceLookupPreferencePage();
		preferenceDialog.select(sourceLookupPreferencePage);
		sourceLookupPreferencePage.setSourceAttachment(
				SourceLookupPreferencePage.SourceAttachmentEnum.NEVER);
		preferenceDialog.ok();
		new WaitUntil(new ShellWithTextIsActive(originalShellText));
	}
	
	public void refreshProject(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).refresh();
	}
}