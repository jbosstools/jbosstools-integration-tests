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

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.archives.reddeer.archives.ui.ArchivePublishDialog;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;
import org.jboss.tools.archives.reddeer.component.Archive;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if deploying an archive via archives view and explorer is possible
 * 
 * @author jjankovi
 *
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@CleanWorkspace
@OpenPerspective(JavaPerspective.class)
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
	
	@InjectRequirement
	protected ServerRequirement requirement;
	
	@BeforeClass
	public static void setup() {
		importArchiveProjectWithoutRuntime(project);
	}
	
	@Test
	public void testDeployingArchiveWithView() {
		
		/* prepare view for testing */
		view = viewForProject(project);
		
		/* publish into server with entered options */
		Archive archive	= view
			.getProject()
			.getArchive(PATH_ARCHIVE_1);
		fillPublishDialog(archive.publishToServer(), false, false);
				
		/* test archive is deployed */
		assertArchiveIsDeployed(project + "/" + ARCHIVE_NAME_1);
		
		/* remove archive from pre-configured server */
		removeArchiveFromServer(project + "/" + ARCHIVE_NAME_1);
		
		/* select the project again - workaround when switching views */
		view = viewForProject(project);
		
		/* edit publish setting - always publish option */
		fillPublishDialog(archive.editPublishSettings(), true, false);
		
		/* publish into server without dialog appears */
		archive.publishToServer();
		
		/* test archive is deployed */
		assertArchiveIsDeployed(project + "/" + ARCHIVE_NAME_1);
	}

	@Test
	public void testDeployingArchiveWithExplorer() {
		
		/* prepare explorer for testing */
		ProjectArchivesExplorer explorer = explorerForProject(project);
		
		/* publish into server with entered options */
		Archive archive = explorer
				.getArchive(PATH_ARCHIVE_2);
		fillPublishDialog(archive.publishToServer(), false, false);
		
		/* test archive is deployed */
		assertArchiveIsDeployed(project + "/" + ARCHIVE_NAME_2);
		
		/* remove archive from pre-configured server */
		removeArchiveFromServer(project + "/" + ARCHIVE_NAME_2);
		
		/* select the project again - workaround when switching views */
		explorer = explorerForProject(project);
		
		/* edit publish setting - always publish option */
		fillPublishDialog(archive.editPublishSettings(), true, false);
		
		/* publish into server without dialog appears */
		archive.publishToServer();
		
		/* test archive is deployed */
		assertArchiveIsDeployed(project + "/" + ARCHIVE_NAME_2);
	}
	
	private void fillPublishDialog(ArchivePublishDialog dialog, boolean alwaysPublish, boolean autodeploy) {
		if (!alwaysPublish && autodeploy) {
			throw new IllegalArgumentException(
					"Cannot autodeploy without always publish option checked");
		}
		dialog.selectServers(requirement.getServerNameLabelText(requirement.getConfig()));
		if (alwaysPublish) dialog.checkAlwaysPublish();
		if (autodeploy) dialog.checkAutoDeploy();
		dialog.finish();
	}
	
	private void removeArchiveFromServer(String archive) {
		ServersView serversView = new ServersView();
		serversView.open();
		ModifyModulesDialog md = serversView.getServer(requirement.getServerNameLabelText(requirement.getConfig())).addAndRemoveModules();
		ModifyModulesPage mp = md.getFirstPage();
		mp.remove(archive);
		md.finish();
	
	}
	
	private void assertArchiveIsDeployed(String archive) {
		ServersView sview = new ServersView();
		sview.open();
		boolean found = false;
		for(TreeItem i: new DefaultTree().getItems()){
			if(i.getText().contains(requirement.getServerNameLabelText(requirement.getConfig()))){
				for (TreeItem node : i.getItems()) {
					System.out.println(node.getText());
					System.out.println(archive);
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
