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
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.archives.reddeer.archives.ui.ArchivePublishDialog;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.reddeer.component.Archive;
import org.jboss.tools.runtime.as.ui.bot.test.Activator;
import org.jboss.tools.runtime.as.ui.bot.test.download.RuntimeDownloadTestUtility;
import org.jboss.tools.runtime.as.ui.bot.test.parametized.server.ServerRuntimeUIConstants;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.util.DetectRuntimeTemplate;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests if deploying an archive via archives view and explorer is possible
 * 
 * @author jjankovi
 *
 */
@RunWith(RedDeerSuite.class)
public class DeployingArchiveTest extends ArchivesTestBase {
	private static String SMOKETEST_TYPE = ServerRuntimeUIConstants.SMOKETEST_DOWNLOADS[0];

	private static String projectName = "DeployingArchiveTest";
	private static final String ARCHIVE_NAME_1 = projectName + "a.jar";
	private static final String ARCHIVE_NAME_2 = projectName + "b.jar";
	private static final String PATH_SUFFIX = " [/" + projectName + "]"; 
	private static final String PATH_ARCHIVE_1 = ARCHIVE_NAME_1 + PATH_SUFFIX;
	private static final String PATH_ARCHIVE_2 = ARCHIVE_NAME_2 + PATH_SUFFIX;	
	
	
	@BeforeClass
	public static void setup() {
		createJavaProject(projectName);
		addArchivesSupport(projectName);
		createArchive(projectName, ARCHIVE_NAME_1, true);
		createArchive(projectName, ARCHIVE_NAME_2, true);
		
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
    
	@Test
	public void testDeployingArchiveWithView() {
		view = viewForProject(projectName);
		Archive archive	= view.getProject(projectName).getArchive(PATH_ARCHIVE_1);
		fillPublishDialog(archive.publishToServer(), false, false);
		assertArchiveIsDeployed(projectName + "/" + ARCHIVE_NAME_1);
		removeArchiveFromServer(projectName + "/" + ARCHIVE_NAME_1);
		view = viewForProject(projectName);
		fillPublishDialog(archive.editPublishSettings(), true, false);
		archive.publishToServer();
		assertArchiveIsDeployed(projectName + "/" + ARCHIVE_NAME_1);
	}

	@Test
	public void testDeployingArchiveWithExplorer() {
		ProjectArchivesExplorer explorer = explorerForProject(projectName);
		Archive archive = explorer.getArchive(PATH_ARCHIVE_2);
		fillPublishDialog(archive.publishToServer(), false, false);
		assertArchiveIsDeployed(projectName + "/" + ARCHIVE_NAME_2);
		removeArchiveFromServer(projectName + "/" + ARCHIVE_NAME_2);
		explorer = explorerForProject(projectName);
		fillPublishDialog(archive.editPublishSettings(), true, false);
		archive.publishToServer();
		assertArchiveIsDeployed(projectName + "/" + ARCHIVE_NAME_2);
	}
	
	private void fillPublishDialog(ArchivePublishDialog dialog, boolean alwaysPublish, boolean autodeploy) {
		if (!alwaysPublish && autodeploy) {
			throw new IllegalArgumentException(
					"Cannot autodeploy without always publish option checked");
		}
		
    	String serverName = ServerRuntimeUIConstants.getServerName(SMOKETEST_TYPE);
		dialog.selectServers(serverName);
		if (alwaysPublish) dialog.checkAlwaysPublish();
		if (autodeploy) dialog.checkAutoDeploy();
		dialog.finish();
	}
	
	private void removeArchiveFromServer(String archive) {
		ServersView serversView = new ServersView();
		serversView.open();
    	String serverName = ServerRuntimeUIConstants.getServerName(SMOKETEST_TYPE);
		ModifyModulesDialog md = serversView.getServer(serverName).addAndRemoveModules();
		ModifyModulesPage mp = new ModifyModulesPage();
		mp.remove(archive);
		md.finish();
	
	}
	
	private void assertArchiveIsDeployed(String archive) {
		ServersView sview = new ServersView();
		sview.open();
		boolean found = false;
    	String serverName = ServerRuntimeUIConstants.getServerName(SMOKETEST_TYPE);

		for(TreeItem i: new DefaultTree().getItems()){
			if(i.getText().contains(serverName)){
				for (TreeItem node : i.getItems()) {
					String[] nodeParsed = node.getText().split(" ");
					if (nodeParsed[0].equals(archive)) {
						found = true;
						break;
					}
				}
			}
		}
		assertTrue(archive + " was not deployed", found);
	}
}
