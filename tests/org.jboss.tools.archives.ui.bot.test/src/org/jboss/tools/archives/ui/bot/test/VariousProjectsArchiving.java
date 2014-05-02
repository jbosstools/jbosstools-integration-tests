/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.After;
import org.junit.Test;

/**
 * Tests if creating an archive for various types of projects is possible
 * without archive errors
 * 
 * @author jjankovi
 *
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
public class VariousProjectsArchiving extends ArchivesTestBase {
	
	@InjectRequirement
	protected ServerRequirement requirement;
	
	@After
	public void checkErrorLog() {
		assertClearArchivesErrorLog();
	}
	
	@Test
	public void testDynamicWebProject() {
		String project = "pr1";
		
		/* create dynamic web project */
		createDynamicWebProject(project, requirement.getRuntimeNameLabelText(requirement.getConfig()));
		
		/* clear error view before creating an archive */
		clearErrorView();
		
		/* open view for project */
		view = viewForProject(project);
		
		/* create archive */
		view
			.getProject()
			.newJarArchive()
			.finish();
		
		/* test if archive was created and no error was thrown*/
		assertArchiveIsInView(view, project + ".jar [/" + project + "]");
	}
	
	@Test
	public void testEJBProject() {
		String project = "pr2";
		
		/* create ejb project */
		createEJBProject(project);
		
		/* clear error view before creating an archive */
		clearErrorView();
		
		/* open view for project */
		view = viewForProject(project);
		
		/* create archive */
		view
			.getProject()
			.newJarArchive()
			.finish();
		
		/* test if archive was created and no error was thrown*/
		assertArchiveIsInView(view, project + ".jar [/" + project + "]");
	}
	
	private void createDynamicWebProject(String project, String targetRuntime) {
		WebProjectWizard ww = new WebProjectWizard();
		ww.open();
		WebProjectFirstPage fp = (WebProjectFirstPage)ww.getWizardPage(0);
		fp.setProjectName(project);
		fp.setTargetRuntime(targetRuntime);
		ww.finish();
	}
	
	private void createEJBProject(String project) {
		createProject(project,"EJB","EJB Project");
	}
	
	private void createProject(String project, String... wizard) {
		new ShellMenu("File","New","Other...").select();
		new DefaultShell("New");
		new DefaultTreeItem(wizard).select();
		new PushButton("Next >").click();
		Text t = new LabeledText("Project name:");
		t.setText(project);
		new PushButton("Finish").click();
		handlePerspectivePopUpDialog();
		new WaitWhile(new JobIsRunning());
	}
	
	private void handlePerspectivePopUpDialog() {
		try {
			new DefaultShell(IDELabel.Shell.OPEN_ASSOCIATED_PERSPECTIVE);
			new PushButton(IDELabel.Button.NO).click();
		} catch (SWTLayerException exc) {
			//do nothing here
		}
	}
	
	private void assertClearArchivesErrorLog() {
		
		assertTrue("Error log contains archive errors", 
				countOfArchivesErrors() == 0);
	}
	
	private int countOfArchivesErrors() {
		LogView lv = new LogView();
		lv.open();
		int archivesErrorsCount = 0;
		for(LogMessage msg: lv.getErrorMessages()){
			String pluginId = msg.getPlugin();
			if (pluginId.contains("org.jboss.ide.eclipse.archives")) {
				archivesErrorsCount++;
			}
		}
		return archivesErrorsCount;
	}
}
