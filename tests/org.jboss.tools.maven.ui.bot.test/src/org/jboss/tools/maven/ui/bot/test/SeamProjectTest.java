/*************************************************************************************
 * Copyright (c) 2008-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/

package org.jboss.tools.maven.ui.bot.test;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.datatools.ui.DriverDefinition;
import org.jboss.reddeer.eclipse.datatools.ui.DriverTemplate;
import org.jboss.reddeer.eclipse.datatools.ui.preference.DriverDefinitionPreferencePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionWizard;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectHasErrors;
import org.jboss.tools.seam.reddeer.preferences.SeamPreferencePage;
import org.jboss.tools.seam.reddeer.wizards.SeamProjectDialog;
import org.jboss.tools.seam.reddeer.wizards.SeamProjectFifthPage;
import org.jboss.tools.seam.reddeer.wizards.SeamProjectFirstPage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jboss.tools.seam.reddeer.perspective.SeamPerspective;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
/**
 * @author Rastislav Wagner
 * 
 */
@CleanWorkspace
@OpenPerspective(SeamPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class SeamProjectTest extends AbstractMavenSWTBotTest {
    
    @InjectRequirement
    private ServerRequirement sr;

	public static final String SEAM_WEB_PROJECT = "seamWeb";
	public static final String SEAM_EAR_PROJECT = "seamEar";
	public static final String SEAM_2_3 = System.getProperty("jbosstools.test.seam.2.3.0.home");
	public static final String SEAM_2_3_NAME = "jboss-seam-2.3.0.Final";
	public static final String SEAM_2_2 = System.getProperty("jbosstools.test.seam.2.2.0.home");
	public static final String SEAM_2_2_NAME = "jboss-seam-2.2.0.GA";
	public static final String SEAM_2_1 = System.getProperty("jbosstools.test.seam.2.1.0.home");
	public static final String SEAM_2_1_NAME = "jboss-seam-2.1.2";
	public static final String CONNECTION_PROFILE_NAME = "DefaultDS";
	public static final String HSQL_DRIVER_DEFINITION_ID ="DriverDefn.Hypersonic DB";
	public static final String HSQL_DRIVER_NAME ="Hypersonic DB";
	public static final String HSQL_DRIVER_TEMPLATE_ID = "org.eclipse.datatools.enablement.hsqldb.1_8.driver";
	public static final String DTP_DB_URL_PROPERTY_ID = "org.eclipse.datatools.connectivity.db.URL";
	public static final String HSQL_PROFILE_ID = "org.eclipse.datatools.enablement.hsqldb.connectionProfile";
	public static final String HSQLDB_DRIVER_LOCATION =System.getProperty("hsqldb.driver");
	
	public static final String ACCEPTED_ERROR_TYPE = "Maven Project Build Lifecycle Mapping Problem";
	
	public static final String SEAM23_NAME="Seam2.3";
	public static final String SEAM22_NAME="Seam2.2";
	public static final String SEAM21_NAME="Seam2.1";

	@BeforeClass
	public static void setup() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		SeamPreferencePage sp = new SeamPreferencePage();
		preferenceDialog.select(sp);
		sp.addRuntime(SEAM23_NAME, SEAM_2_3, "2.3");
		sp.addRuntime(SEAM22_NAME, SEAM_2_2, "2.2");
		sp.addRuntime(SEAM21_NAME, SEAM_2_1, "2.1");
		preferenceDialog.ok();
		
		DriverTemplate dt = new DriverTemplate("HSQLDB JDBC Driver", "1.8");
		
		DriverDefinition dd = new DriverDefinition();
        dd.setDriverName("HSQLDB JDBC Database");
        dd.setDriverTemplate(dt);
        dd.setDriverLibrary(HSQLDB_DRIVER_LOCATION);
		
		preferenceDialog.open();
		DriverDefinitionPreferencePage dpref = new DriverDefinitionPreferencePage();
		preferenceDialog.select(dpref);
		
		DriverDefinitionWizard ddw = dpref.addDriverDefinition();
		ddw.create(dd);
		preferenceDialog.ok();
		
		ConnectionProfileWizard cw = new ConnectionProfileWizard();
		cw.open();
		new DefaultTable().select("HSQLDB");
		new NextButton().click();
		cw.finish();
	}
	
	@Test
	public void createSeam23WebProjectTest(){
		createSeamProject(SEAM_WEB_PROJECT+"23", SEAM_2_3_NAME, "2.3", false);
		new WaitWhile(new ProjectHasErrors(SEAM_WEB_PROJECT+"23", null), TimePeriod.LONG);
		deleteProjects(true,false);
	}
	
	@Test
	public void createSeam23EarProjectTest(){
		createSeamProject(SEAM_EAR_PROJECT+"23", SEAM_2_3_NAME, "2.3", true);
		new WaitWhile(new ProjectHasErrors(SEAM_EAR_PROJECT+"23", null), TimePeriod.LONG);
		deleteProjects(true,false);
	}
	
	@Test
	public void createSeam22WebProjectTest(){
		createSeamProject(SEAM_WEB_PROJECT+"22", SEAM_2_2_NAME, "2.2",false);
		new WaitWhile(new ProjectHasErrors(SEAM_WEB_PROJECT+"22", ACCEPTED_ERROR_TYPE), TimePeriod.LONG);
		deleteProjects(true,true);
	}

	@Test
	public void createSeam22EarProjectTest(){
		createSeamProject(SEAM_EAR_PROJECT+"22", SEAM_2_2_NAME, "2.2",true);
		new WaitWhile(new ProjectHasErrors(SEAM_EAR_PROJECT+"22", ACCEPTED_ERROR_TYPE), TimePeriod.LONG);
		deleteProjects(true,true);
	}

	private void createSeamProject(String projectName, String seamRuntime, String seamVersion, boolean EAR){
		SeamProjectDialog sd = new SeamProjectDialog();
		sd.open();
		SeamProjectFirstPage sf = (SeamProjectFirstPage)sd.getWizardPage(0);
		sf.setProjectName(projectName);
		sf.setRuntime(sr.getRuntimeNameLabelText(sr.getConfig()));
		//sf.setServer(serverName);
		sf.activateFacet("Seam", seamVersion);
		sf.activateFacet("JBoss Maven Integration", null);
		SeamProjectFifthPage sfp = (SeamProjectFifthPage)sd.getWizardPage(5);
		sfp.setSeamRuntime(seamRuntime);
		sfp.toggleEAR(EAR);
		sfp.setConnectionProfile("New HSQLDB");
		sd.finish();
	}
	
}