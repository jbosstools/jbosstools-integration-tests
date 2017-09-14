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

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.exception.EclipseLayerException;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.jsf.reddeer.ui.ImportJSFWarWizard;
import org.jboss.tools.jsf.reddeer.ui.ImportWebWarWizardPage;
import org.jboss.tools.jsf.reddeer.ui.WebComponentExportWizardPage;
import org.jboss.tools.jsf.ui.test.requirement.DoNotUseVPERequirement.DoNotUseVPE;
import org.jboss.tools.jsf.ui.test.utils.JSFTestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@DoNotUseVPE
@JBossServer(state = ServerRequirementState.RUNNING)
public class ExportImportWARTest {

	private static final String PROJECT_NAME = "JSFProject";
	private static final String NEW_PROJECT_NAME = "ImportedJSFProject";
	private static final String WAR_FILE_LOCATION = "target/exported.war";
	private static ProjectExplorer projectExplorer;

	@InjectRequirement
	static ServerRequirement serverReq;

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.EAP());
	}
	
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
		ServersView2 serversView = new ServersView2();
		serversView.open();
		Server server = serversView.getServer(serverReq.getServerNameLabelText());
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
		ImportWebWarWizardPage importPage = new ImportWebWarWizardPage(importDialog);
		importPage.setWarLocation(new File(WAR_FILE_LOCATION).getAbsolutePath());
		importPage.setName(NEW_PROJECT_NAME);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
		importDialog.finish();
	}

	private void verifyExportedProject() {
		File file = new File(WAR_FILE_LOCATION);
		assertTrue(file.exists());
	}

	private void exportProject() {
		new ContextMenuItem("Export", "WAR file").select();
		WebComponentExportWizardPage exportDialog = new WebComponentExportWizardPage();
		exportDialog.setWebProject(PROJECT_NAME);
		exportDialog.setDestination(new File(WAR_FILE_LOCATION).getAbsolutePath());
		exportDialog.toggleOverwriteExistingFile(true);
		exportDialog.finish();
	}
}
