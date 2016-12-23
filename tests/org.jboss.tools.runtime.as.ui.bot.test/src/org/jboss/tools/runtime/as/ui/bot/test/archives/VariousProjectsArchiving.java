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
package org.jboss.tools.runtime.as.ui.bot.test.archives;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jboss.ide.eclipse.archives.ui.test.bot.ArchivesTestBase;
import org.jboss.reddeer.eclipse.jst.ejb.ui.EjbProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.ejb.ui.EjbProjectWizard;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.as.ui.bot.test.Activator;
import org.jboss.tools.runtime.as.ui.bot.test.download.RuntimeDownloadTestUtility;
import org.jboss.tools.runtime.as.ui.bot.test.parametized.server.ServerRuntimeUIConstants;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.util.DetectRuntimeTemplate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests if creating an archive for various types of projects is possible
 * without archive errors
 * 
 * @author jjankovi
 *
 */
@RunWith(RedDeerSuite.class)
public class VariousProjectsArchiving extends ArchivesTestBase {
	private static String SMOKETEST_TYPE = ServerRuntimeUIConstants.SMOKETEST_DOWNLOADS[0];
	
	@BeforeClass
	public static void setup() {
		File f = Activator.getDownloadFolder(SMOKETEST_TYPE);
		if( !f.exists() || f.list() == null || f.list().length == 0 ) {
	        RuntimeDownloadTestUtility util = new RuntimeDownloadTestUtility(f);
			util.downloadRuntimeNoCredentials(SMOKETEST_TYPE);
		} else {
	    	DetectRuntimeTemplate.detectRuntime(f.getAbsolutePath(), 
	    			ServerRuntimeUIConstants.getRuntimesForDownloadable(SMOKETEST_TYPE));
	    	DetectRuntimeTemplate.removePath(f.getAbsolutePath());
		}
	}

    @AfterClass
    public static void postClass() {
    	new RuntimeDownloadTestUtility(Activator.getStateFolder().toFile()).clean(false);
    }
	
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
    	String serverName = ServerRuntimeUIConstants.getServerName(SMOKETEST_TYPE);
		createDynamicWebProject(project, serverName + " Runtime");
		
		deleteErrorView();
		view = viewForProject(project);
		view.getProject(project).newJarArchive().finish();
		assertArchiveIsInView(project, view, project + ".jar [/" + project + "]");
	}
	
	@Test
	public void testEJBProject() {
		String project = "pr2";
    	String serverName = ServerRuntimeUIConstants.getServerName(SMOKETEST_TYPE);
		createEJBProject(project, serverName + " Runtime");
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
