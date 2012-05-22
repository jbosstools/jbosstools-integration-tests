/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.ui.bot.test;

import java.util.List;

import org.jboss.tools.archives.ui.bot.test.dialog.ArchivePublishSettingsDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.EditArchiveDialog;
import org.jboss.tools.archives.ui.bot.test.dialog.NewJarDialog;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.entity.JavaProjectEntity;
import org.junit.Test;

@Require(perspective = "Java", 
		 server = @Server(state = ServerState.NotRunning))
/**
 * 
 * @author jjankovi
 *
 */
public class ExampleTest extends SWTTestExt {
	
	@Test
	public void testDefault() {
		
//		String projectName = "project";
//		String archiveNodeText = projectName + ".jar" + " [/" + projectName + "]";
//		
//		/* create new java project */
//		JavaProjectEntity projectEntity = new JavaProjectEntity();
//		projectEntity.setProjectName(projectName);
//		eclipse.createJavaProject(projectEntity);
//		
//		/* open archive view */
//		ProjectArchivesView view = new ProjectArchivesView();
//		view.show();
//		projectExplorer.selectProject(projectName);
//		
//		/* create jar archive with default values */
//		NewJarDialog dialog = view.createNewJarArchive(projectName);
//		dialog.finish();
//		
//		/* build the project with Project Archives view */
//		view.buildProjectFull(projectName);
//		
//		/* build the archive with Project Archives view */
//		view.buildArchiveFull(projectName, archiveNodeText);
//		
//		/* edit the archive with no change */
//		EditArchiveDialog dialog2 = view.editArchive(projectName, archiveNodeText);
//		dialog2.cancel();
//		
//		/* open publish dialog */
//		ArchivePublishSettingsDialog publishDialog = 
//				view.publishToServer(projectName, archiveNodeText);
//		
//		/* try some actions in dialog */
//		publishDialog.checkAlwaysPublish();
//		publishDialog.checkAutoDeploy();
//		publishDialog.uncheckAutoDeploy();
//		publishDialog.uncheckAlwaysPublish();
//		
//		List<String> servers = publishDialog.getAllServersInDialog();
//		
//		assertTrue(servers.size() == 1);
//		assertTrue(servers.get(0).equals(configuredState.getServer().name));
//		
//		String[] strArray = new String[servers.size()];
//		servers.toArray(servers.toArray(strArray));
//		publishDialog.selectServers(strArray);
//		publishDialog.unselectServers(strArray);
//		publishDialog.cancel();
//		
//		/* delete the archive */
//		view.deleteArchive(projectName, archiveNodeText);
	}

}
