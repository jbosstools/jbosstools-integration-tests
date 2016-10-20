/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jsf.ui.test.project;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.jsf.reddeer.ui.ImportJSFWarWizard;
import org.jboss.tools.jsf.reddeer.ui.ImportWebWarWizardPage;
import org.jboss.tools.jsf.reddeer.ui.WebComponentExportWizardPage;
import org.jboss.tools.jsf.ui.test.requirement.DoNotUseVPERequirement.DoNotUseVPE;
import org.jboss.tools.jsf.ui.test.utils.JSFTestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@DoNotUseVPE
@JBossServer(type = ServerReqType.EAP7x, state = ServerReqState.RUNNING)
public class ExportImportWARTest {

    private static final String PROJECT_NAME = "JSFProject";
    private static final String NEW_PROJECT_NAME = "ImportedJSFProject";
    private static final String WAR_FILE_LOCATION = "target/exported.war";
    private static ProjectExplorer projectExplorer;

    @InjectRequirement
    static ServerRequirement serverReq;

    @BeforeClass
    public static void setupClass() {
	JSFTestUtils.createJSFProject(PROJECT_NAME, "JSF 2.2", "JSFKickStartWithoutLibs");
	projectExplorer = new ProjectExplorer();
    }

    @AfterClass
    public static void teardownClass() {
	removeModulesFromServer();

	projectExplorer.open();
	projectExplorer.deleteAllProjects(true);
    }

    @Test
    public void exportToWarAndImportBackAgainTest() {
	projectExplorer.open();
	Project project = projectExplorer.getProject(PROJECT_NAME);
	project.select();

	exportProject();
	verifyExportedProject();

	importProject();
	verifyImportedProject();
    }

    private static void removeModulesFromServer() {
	ServersView serversView = new ServersView();
	serversView.open();
	Server server = serversView.getServer(serverReq.getServerNameLabelText(serverReq.getConfig()));
	server.getModules().forEach(serverModule -> serverModule.remove());
    }

    private void verifyImportedProject() {
	projectExplorer.open();
	try {
	    projectExplorer.getProject(NEW_PROJECT_NAME);
	} catch (EclipseLayerException ex) {
	    fail("Project is not imported into workspace.");
	}
    }

    private void importProject() {
	ImportJSFWarWizard importDialog = new ImportJSFWarWizard();
	importDialog.open();
	ImportWebWarWizardPage importPage = new ImportWebWarWizardPage();
	importPage.setWarLocation(new File(WAR_FILE_LOCATION).getAbsolutePath());
	importPage.setName(NEW_PROJECT_NAME);
	importDialog.finish();
    }

    private void verifyExportedProject() {
	File file = new File(WAR_FILE_LOCATION);
	assertTrue(file.exists());
    }

    private void exportProject() {
	new ContextMenu("Export", "WAR file").select();
	WebComponentExportWizardPage exportDialog = new WebComponentExportWizardPage();
	exportDialog.setWebProject(PROJECT_NAME);
	exportDialog.setDestination(new File(WAR_FILE_LOCATION).getAbsolutePath());
	exportDialog.toggleOverwriteExistingFile(true);
	exportDialog.finish();
    }
}
