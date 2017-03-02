
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
package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.deploy.DeployOnServer;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.forge.reddeer.ui.wizard.EntitiesFromTablesWizardFirstPage;
import org.jboss.tools.forge.reddeer.ui.wizard.EntitiesFromTablesWizardSecondPage;
import org.jboss.tools.forge.ui.bot.test.util.DatabaseUtils;
import org.jboss.tools.forge.ui.bot.test.util.ScaffoldType;
import org.jboss.tools.hibernate.reddeer.wizard.NewDSXMLWizard;
import org.jboss.tools.hibernate.reddeer.wizard.WizardNewDSXMLFileCreationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * 
 * @author jkopriva@redhat.com
 *
 */
@JBossServer(state = ServerReqState.RUNNING, type = ServerReqType.WILDFLY10x)
public class DeployScaffoldDBTest extends WizardTestBase {

	private List<String> tableNames = new ArrayList<String>();

	private static final String PACKAGE = GROUPID + ".model";
	
	@InjectRequirement 
	private static ServerRequirement sr;
	private static final String SERVER_NAME = sr.getServerNameLabelText(sr.getConfig());

	@BeforeClass
	public static void startSakila() {
		startSakilaDatabase();
	}

	@Before
	public void prepare() {
		setUpSakilaDatabase();
	}

	@Test
	public void testDeployScaffoldDBAngular() {
		setProjectAndParameters(ScaffoldType.ANGULARJS);
	}

	@Test
	public void testDeployScaffoldDBFaces() {
		setProjectAndParameters(ScaffoldType.FACES);
	}

	public void setProjectAndParameters(ScaffoldType type) {
		createJBOSSDatasource();
		jpaSetup();
		jpaGenerateEntities();
		generateScaffold(type);
		deployOnServer();
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

	@AfterClass
	public static void stopDB() {
		DatabaseUtils.stopSakilaDB();
	}

	public void createNewProject() {
		newProject(PROJECT_NAME);
	}

	public void createJBOSSDatasource() {
		NewDSXMLWizard wizard = new NewDSXMLWizard();
		wizard.open();
		WizardNewDSXMLFileCreationPage page = new WizardNewDSXMLFileCreationPage();
		page.setConnectionProfile(PROFILE_NAME);
		page.setParentFolder("/" + PROJECT_NAME + "/src/main/resources");
		new PushButton("Finish").click();
	}

	public void jpaSetup() {
		persistenceSetup(PROJECT_NAME);
	}

	public void jpaGenerateEntities() {
		new ProjectExplorer().selectProjects(PROJECT_NAME);
		WizardDialog dialog = getWizardDialog("JPA: Generate Entities From Tables",
				"(JPA: Generate Entities From Tables).*");
		EntitiesFromTablesWizardFirstPage firstPage = new EntitiesFromTablesWizardFirstPage();
		firstPage.setPackage(PACKAGE);
		assertTrue("Missing connection profile selection", firstPage.getAllProfiles().contains(PROFILE_NAME));
		firstPage.setConnectionProfile(PROFILE_NAME);
		dialog.next();

		EntitiesFromTablesWizardSecondPage secondPage = new EntitiesFromTablesWizardSecondPage();
		List<TableItem> tables = secondPage.getAllTables();
		assertFalse("No database tables found", tables.isEmpty());
		for (TableItem item : tables) {
			tableNames.add(item.getText());
		}
		secondPage.selectAll();
		dialog.finish();
	}

	public void generateScaffold(ScaffoldType type) {
		scaffoldSetup(PROJECT_NAME, type);
		createScaffold(PROJECT_NAME, type);
	}

	public void deployOnServer() {
		new DeployOnServer().deployUndeployProjectToServer(PROJECT_NAME, SERVER_NAME);
	}

}