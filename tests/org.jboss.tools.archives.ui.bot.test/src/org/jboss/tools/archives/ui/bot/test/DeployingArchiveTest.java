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

import org.jboss.tools.archives.ui.bot.test.dialog.ArchivePublishSettingsDialog;
import org.jboss.tools.archives.ui.bot.test.explorer.ProjectArchivesExplorer;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
@Require(clearProjects = true, perspective = "Java",
		 server = @Server(state = ServerState.NotRunning, 
		 version = "6.0", operator = ">="))
public class DeployingArchiveTest extends ArchivesTestBase {

	private static String project = "pr3";
	private final String ARCHIVE_NAME_1 = 
			project + "a.jar";
	private final String ARCHIVE_NAME_2 = 
			project + "b.jar";
	private final String PATH_SUFFIX = " [/" + project + "]"; 
	private final String PATH_ARCHIVE_1 = 
			ARCHIVE_NAME_1 + PATH_SUFFIX;
	private final String PATH_ARCHIVE_2 = 
			ARCHIVE_NAME_2 + PATH_SUFFIX;
	
	@BeforeClass
	public static void setup() {
		importProjectWithoutRuntime(project);
		new ProjectArchivesView().show(); // workaround JBIDE-12493
	}
	
	@Test
	public void testDeployingArchiveWithView() {
		
		/* prepare view for testing */
		ProjectArchivesView view = viewForProject(project);
		
		/* publish into server with entered options */
		publishArchiveInView(view, false, false, 
				project, PATH_ARCHIVE_1);
		
		/* test archive is deployed */
		assertArchiveIsDeployed(project + "/" + ARCHIVE_NAME_1);
		
		/* remove archive from pre-configured server */
		removeArchiveFromServer(project + "/" + ARCHIVE_NAME_1);
		
		/* select the project again - workaround when switching views */
		view = viewForProject(project);
		
		/* edit publish setting - always publish option */
		editPublishSettingsArchiveInView(view, true, false, 
				project, PATH_ARCHIVE_1);
		
		/* publish into server without dialog appears */
		publishArchiveInView(view, project, PATH_ARCHIVE_1);
		
		/* test archive is deployed */
		assertArchiveIsDeployed(project + "/" + ARCHIVE_NAME_1);
	}

	@Test
	public void testDeployingArchiveWithExplorer() {
		
		/* prepare explorer for testing */
		ProjectArchivesExplorer explorer = explorerForProject(project);
		
		/* publish into server with entered options */
		publishArchiveInExplorer(explorer, false, false, PATH_ARCHIVE_2);
		
		/* test archive is deployed */
		assertArchiveIsDeployed(project + "/" + ARCHIVE_NAME_2);
		
		/* remove archive from pre-configured server */
		removeArchiveFromServer(project + "/" + ARCHIVE_NAME_2);
		
		/* select the project again - workaround when switching views */
		explorer = explorerForProject(project);
		
		/* edit publish setting - always publish option */
		editPublishSettingsArchiveInExplorer(explorer, true, false, PATH_ARCHIVE_2);
		
		/* publish into server without dialog appears */
		publishArchiveInExplorer(explorer, PATH_ARCHIVE_2);
		
		/* test archive is deployed */
		assertArchiveIsDeployed(project + "/" + ARCHIVE_NAME_2);
	}
	
	private void publishArchiveInView(
			ProjectArchivesView view, String... archivePath) {
		view.publishToServer(false, archivePath);
	}
	
	private void publishArchiveInView(
			ProjectArchivesView view,  
			boolean alwaysPublish, 
			boolean autodeploy, String... archivePath) {
		fillDeployDialogForArchives(true, true, view, 
				null, alwaysPublish, autodeploy, archivePath);
	}
	
	private void publishArchiveInExplorer(
			ProjectArchivesExplorer explorer, String archive) {
		explorer.publishToServer(false, archive);
	}

	private void publishArchiveInExplorer(
			ProjectArchivesExplorer explorer, 
			boolean alwaysPublish,
			boolean autodeploy, String archive) {
		fillDeployDialogForArchives(true, true, null, 
				explorer, alwaysPublish, autodeploy, archive);
	}
	
	private void editPublishSettingsArchiveInView(
			ProjectArchivesView view, boolean alwaysPublish, 
			boolean autodeploy, String... archivePath) {
		fillDeployDialogForArchives(false, true, view, null,
				alwaysPublish, autodeploy, archivePath);
	}
	
	private void editPublishSettingsArchiveInExplorer(
			ProjectArchivesExplorer explorer, 
			boolean alwaysPublish,
			boolean autodeploy, String archive) {
		fillDeployDialogForArchives(false, true, null, 
				explorer, alwaysPublish, autodeploy, archive);
	}
	
	private void fillDeployDialogForArchives(boolean publishContextMenu, 
			boolean returnDialog, ProjectArchivesView view, 
			ProjectArchivesExplorer explorer, boolean alwaysPublish, 
			boolean autodeploy, String... archivePath) {
		
		if (!alwaysPublish && autodeploy) {
			throw new IllegalArgumentException(
					"Cannot autodeploy without always publish option checked");
		}
		if (view == null && explorer == null) {
			throw new IllegalArgumentException(
					"At least one of explorer or view must be provided");
		}
		ArchivePublishSettingsDialog dialog = getDialog(publishContextMenu, 
				view, explorer, returnDialog, archivePath);
		
		dialog.selectServers(configuredState.getServer().name);
		if (alwaysPublish) dialog.checkAlwaysPublish();
		if (autodeploy) dialog.checkAutoDeploy();
		dialog.finish();
	}

	private ArchivePublishSettingsDialog getDialog(boolean publishContextMenu,
			ProjectArchivesView view, ProjectArchivesExplorer explorer,
			boolean returnDialog, String[] archivePath) {
		ArchivePublishSettingsDialog dialog = null;
		if (publishContextMenu) {
			if (view == null) {
				dialog = explorer.publishToServer(returnDialog, archivePath[0]);
			} else {
				dialog = view.publishToServer(returnDialog, archivePath);
			}
		} else {
			if (view == null) {
				dialog = explorer.editPublishSettings(archivePath[0]);
			} else {
				dialog = view.editPublishSettings(archivePath);
			}
		}
		return dialog;
		
	}

	
}
