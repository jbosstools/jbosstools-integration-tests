
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.deploy.DeployOnServer;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.forge.reddeer.ui.wizard.EntitiesFromTablesWizardFirstPage;
import org.jboss.tools.forge.reddeer.ui.wizard.EntitiesFromTablesWizardSecondPage;
import org.jboss.tools.forge.ui.bot.test.util.DatabaseUtils;
import org.jboss.tools.forge.ui.bot.test.util.ScaffoldType;
//import org.jboss.tools.hibernate.reddeer.wizard.NewDSXMLWizard;
//import org.jboss.tools.hibernate.reddeer.wizard.WizardNewDSXMLFileCreationPage;
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
	
	private static String SERVER_NAME = "";
	
	@InjectRequirement 
	private static ServerRequirement sr;

	@BeforeClass
	public static void startSakila() {
		startSakilaDatabase();
		SERVER_NAME = sr.getServerNameLabelText();
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
		jpaSetup(PROJECT_NAME);
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
	/*	NewDSXMLWizard wizard = new NewDSXMLWizard();
		//wizard.open();
		WizardNewDSXMLFileCreationPage page = new WizardNewDSXMLFileCreationPage();
		page.setConnectionProfile(PROFILE_NAME);
		page.setParentFolder("/" + PROJECT_NAME + "/src/main/resources");
		new PushButton("Finish").click();*/
	}

	public void jpaSetup(String projectName) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(projectName); // this will set context for forge
		WizardDialog wd = getWizardDialog("jpa-setup");
		Combo combo = new DefaultCombo();
		combo.setSelection("Wildfly Application Server");
		wd.finish();
		File persistence = new File(WORKSPACE + "/" + projectName + "/src/main/resources/META-INF/persistence.xml");
		assertTrue("persistence.xml file does not exist", persistence.exists());
		updateProject(PROJECT_NAME,true);
	}

	public void jpaGenerateEntities() {
		new ProjectExplorer().selectProjects(PROJECT_NAME);
		WizardDialog dialog = getWizardDialog("JPA: Generate Entities From Tables");
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
		updateProject(PROJECT_NAME,true);
	}

	public void generateScaffold(ScaffoldType type) {
		scaffoldSetup(PROJECT_NAME, type);
		createScaffold(PROJECT_NAME, type);
		updateProject(PROJECT_NAME,true);
	}

	public void deployOnServer() {
		DeployOnServer ds = new DeployOnServer();
		ds.deployProject(PROJECT_NAME, SERVER_NAME);
		ds.restartServer(SERVER_NAME);
		ds.checkDeployedProject(PROJECT_NAME, SERVER_NAME);
		ds.deployUndeployProjectToServer(PROJECT_NAME, SERVER_NAME);
	}
	
	public static void updateProject(String projectName,boolean forceDependencies){
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Maven","Update Project...").select();
		new WaitUntil(new ShellIsAvailable("Update Maven Project"),TimePeriod.LONG);
		new CheckBox("Force Update of Snapshots/Releases").toggle(forceDependencies);
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable("Update Maven Project"),TimePeriod.DEFAULT);
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
	}
}