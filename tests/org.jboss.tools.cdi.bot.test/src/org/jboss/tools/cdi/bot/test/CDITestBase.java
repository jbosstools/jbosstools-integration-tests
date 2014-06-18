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

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.lookup.ShellLookup;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.cdi.bot.test.uiutils.BeansXMLHelper;
import org.jboss.tools.cdi.bot.test.uiutils.CDIProjectHelper;
import org.jboss.tools.cdi.bot.test.uiutils.CDIWizardHelper;
import org.jboss.tools.cdi.bot.test.uiutils.EditorResourceHelper;
import org.jboss.tools.cdi.bot.test.uiutils.QuickFixHelper;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage;
import org.junit.After;
import org.junit.Before;

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class CDITestBase{
	
	protected static final String PROJECT_NAME = "CDIProject";
	protected static final String PACKAGE_NAME = "cdi";
	
	protected static final Logger LOGGER = Logger.getLogger(CDITestBase.class.getName());
	protected static final CDIProjectHelper projectHelper = new CDIProjectHelper(); 
	protected static final BeansXMLHelper beansHelper = new BeansXMLHelper();
	protected static final CDIWizardHelper wizard = new CDIWizardHelper();
	protected static final EditorResourceHelper editResourceUtil = new EditorResourceHelper();
	protected static final QuickFixHelper quickFixHelper = new QuickFixHelper();

	@Before
	public void prepareWorkspace() {
		prepareWorkspaceStatic(getProjectName());
	}
	
	protected static void prepareWorkspaceStatic(String projectName) {
		if (!projectHelper.projectExists(projectName)) {
			importCDITestProject(projectName);
		}
	}

	@After
	public void waitForJobs() {
		new WaitWhile(new JobIsRunning());
	}
		
	protected String getProjectName() {
		return PROJECT_NAME;
	}
	
	protected String getPackageName() {
		return PACKAGE_NAME;
	}
	
	protected static void importCDITestProject(String projectName) {
		String location = "resources/projects/" + projectName;
		importCDITestProject(projectName, location);
	}
	
	protected static void importCDITestProject(String projectName, 
			String projectLocation) {
		
		ExternalProjectImportWizardDialog iDialog = new ExternalProjectImportWizardDialog();
		iDialog.open();
		WizardProjectsImportPage fPage = iDialog.getFirstPage();
		fPage.copyProjectsIntoWorkspace(true);
		try {
			fPage.setRootDirectory((new File(projectLocation)).getParentFile().getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fPage.selectProjects(projectName);
		iDialog.finish();
	}
	
	protected static void disableSourceLookup() {
		// wait for some shell to get activated
		ShellLookup.getInstance().getActiveShell();
		String originalShellText = new DefaultShell().getText();
		SourceLookupPreferencePage sourceLookupPreferencePage = new SourceLookupPreferencePage();
		sourceLookupPreferencePage.open();
		sourceLookupPreferencePage.setSourceAttachment(
				SourceLookupPreferencePage.SourceAttachmentEnum.NEVER);
		sourceLookupPreferencePage.ok();
		new WaitUntil(new ShellWithTextIsActive(originalShellText));
	}
}