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
import org.jboss.reddeer.eclipse.jst.ejb.ui.EjbProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.ejb.ui.EjbProjectWizard;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests if creating an archive for various types of projects is possible
 * without archive errors
 * 
 * @author jjankovi
 *
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1, cleanup=false)
public class VariousProjectsArchiving extends ArchivesTestBase {
	
	@InjectRequirement
	protected ServerRequirement requirement;
	
	@After
	public void checkErrorLog() {
		assertTrue("Error log contains archive errors", countOfArchivesErrors() == 0);
	}
	
	@Before
	public void deleteLogView(){
		deleteErrorView();
	}
	
	@Test
	public void testDynamicWebProject() {
		String project = "pr1";
		createDynamicWebProject(project, requirement.getRuntimeNameLabelText(requirement.getConfig()));
		
		deleteErrorView();
		view = viewForProject(project);
		view.getProject(project).newJarArchive().finish();
		assertArchiveIsInView(project, view, project + ".jar [/" + project + "]");
	}
	
	@Test
	public void testEJBProject() {
		String project = "pr2";
		
		createEJBProject(project, requirement.getRuntimeNameLabelText(requirement.getConfig()));
		deleteErrorView();
		view = viewForProject(project);
		view.getProject(project).newJarArchive().finish();
		
		assertArchiveIsInView(project, view, project + ".jar [/" + project + "]");
	}
	
	private void createDynamicWebProject(String project, String targetRuntime) {
		WebProjectWizard ww = new WebProjectWizard();
		ww.open();
		WebProjectFirstPage fp = new WebProjectFirstPage();
		fp.setProjectName(project);
		fp.setTargetRuntime(targetRuntime);
		ww.finish();
	}
	
	private void createEJBProject(String project, String targetRuntime) {
		EjbProjectWizard ejbw = new EjbProjectWizard();
		ejbw.open();
		EjbProjectFirstPage fp = new EjbProjectFirstPage();
		fp.setProjectName(project);
		fp.setTargetRuntime(targetRuntime);
		ejbw.finish();
	}
	
	private int countOfArchivesErrors() {
		LogView lv = new LogView();
		lv.open();
		int archivesErrorsCount = 0;
		for(LogMessage msg: lv.getErrorMessages()){
			String pluginId = msg.getPlugin();
			if (pluginId.contains("org.jboss.ide.eclipse.archives")) {
				System.out.println(msg.getMessage());
				System.out.println(msg.getPlugin());
				System.out.println(msg.getStackTrace());
				archivesErrorsCount++;
			}
		}
		return archivesErrorsCount;
	}
}
