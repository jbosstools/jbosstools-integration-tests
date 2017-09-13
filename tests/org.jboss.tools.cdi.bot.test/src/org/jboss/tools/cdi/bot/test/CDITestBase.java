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

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.lookup.ShellLookup;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectFirstPage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.condition.EditorIsDirty;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIProjectWizard;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.uiutils.BeansHelper;
import org.jboss.tools.cdi.reddeer.uiutils.BeansXMLHelper;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.jboss.tools.cdi.reddeer.uiutils.OpenOnHelper;
import org.jboss.tools.cdi.reddeer.uiutils.ProjectHelper;
import org.jboss.tools.cdi.reddeer.uiutils.ValidationHelper;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class CDITestBase {

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

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

	@Before
	public void prepareWorkspace() {
		if (!projectHelper.projectExists(PROJECT_NAME)) {
			CDIProjectWizard cw = new CDIProjectWizard();
			cw.open();
			WebProjectFirstPage fp = new WebProjectFirstPage(cw);
			fp.setProjectName(PROJECT_NAME);
			fp.setTargetRuntime(sr.getRuntimeNameLabelText());
			cw.finish();
			new WaitUntil(new JobIsRunning(), TimePeriod.DEFAULT, false);
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}
	}

	@After
	public void waitForJobs() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@AfterClass
	public static void cleanUp() {
		new WaitWhile(new JobIsRunning());
		EditorHandler.getInstance().closeAll(false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for (Project p : pe.getProjects()) {
			try {
				org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
			} catch (RuntimeException ex) {
				AbstractWait.sleep(TimePeriod.DEFAULT);
				org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
			}
		}
	}

	protected void deleteAllProjects() {
		new WaitWhile(new JobIsRunning());
		EditorHandler.getInstance().closeAll(false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for (Project p : pe.getProjects()) {
			try {
				org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
			} catch (Exception ex) {
				AbstractWait.sleep(TimePeriod.DEFAULT);
				if (!p.getTreeItem().isDisposed()) {
					org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
				}
			}
		}
	}

	protected static String readFile(String path) {
		Scanner s = null;
		try {
			s = new Scanner(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			fail("unable to find file " + path);
		}
		Scanner s1 = s.useDelimiter("\\A");
		String file = s.next();
		s.close();
		s1.close();
		return file;

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
		SourceLookupPreferencePage sourceLookupPreferencePage = new SourceLookupPreferencePage(preferenceDialog);
		preferenceDialog.select(sourceLookupPreferencePage);
		sourceLookupPreferencePage.setSourceAttachment(SourceLookupPreferencePage.SourceAttachmentEnum.NEVER);
		preferenceDialog.ok();
		new WaitUntil(new ShellIsAvailable(originalShellText));
	}

	public void refreshProject() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).refresh();
	}

	/**
	 * Set bean discovery mode. This feature is available since CDI 1.1.<br>
	 * Bean discovery mode may be:<br>
	 * <ul>
	 * <li>none</li>
	 * <li>annotated</li>
	 * <li>all</li>
	 * </ul>
	 * 
	 * @param beanDiscoveryMode
	 *            bean discovery mode
	 */
	public void prepareBeanXml(String beanDiscoveryMode, boolean replaceBeansTag) {
		boolean editorShouldBeDirty = false;
		
		EditorPartWrapper beansEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beansEditor.activateTreePage();
		
		if (!beansEditor.getBeanDiscoveryMode().equals(beanDiscoveryMode)) {
			beansEditor.selectBeanDiscoveryMode(beanDiscoveryMode);
			editorShouldBeDirty = true;
		}
		
		if (replaceBeansTag) {
			beansEditor.activateSourcePage();
			new EditorResourceHelper().replaceInEditor("/>", "></beans>", false);
			editorShouldBeDirty = true;
		}
		
		if (editorShouldBeDirty) {
			new WaitUntil(new EditorIsDirty(beansEditor), false);
			beansEditor.save();
			beansEditor.close();
			new WaitUntil(new JobIsRunning(), false);
			new WaitWhile(new JobIsRunning(), false);
		}
	}
}