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

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.datatools.connectivity.ConnectionProfileConstants;
import org.eclipse.datatools.connectivity.ConnectionProfileException;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.db.generic.IDBConnectionProfileConstants;
import org.eclipse.datatools.connectivity.db.generic.IDBDriverDefinitionConstants;
import org.eclipse.datatools.connectivity.drivers.DriverInstance;
import org.eclipse.datatools.connectivity.drivers.DriverManager;
import org.eclipse.datatools.connectivity.drivers.IDriverMgmtConstants;
import org.eclipse.datatools.connectivity.drivers.IPropertySet;
import org.eclipse.datatools.connectivity.drivers.PropertySetImpl;
import org.eclipse.datatools.connectivity.drivers.models.TemplateDescriptor;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.dialog.seam.SeamPreferencePage;
import org.jboss.tools.maven.ui.bot.test.dialog.seam.SeamProjectDialog;
import org.jboss.tools.maven.ui.bot.test.dialog.seam.SeamProjectFifthPage;
import org.jboss.tools.maven.ui.bot.test.dialog.seam.SeamProjectFirstPage;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectHasErrors;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
public class SeamProjectTest extends AbstractMavenSWTBotTest {

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

	@BeforeClass
	public static void setup() throws ConnectionProfileException, IOException {
		setPerspective("Seam");
		SeamPreferencePage sp = new SeamPreferencePage();
		sp.open();
		sp.addRuntime(SEAM23_NAME, SEAM_2_3, "2.3");
		sp.addRuntime(SEAM22_NAME, SEAM_2_2, "2.2");
		sp.ok();
		createDriver(JBOSS_AS_7_1,"HSQLDB_DRIVER_LOCATION");
	}
	
	@Test
	public void createSeam23WebProjectTest(){
		createSeamProject(SEAM_WEB_PROJECT+"23", SEAM23_NAME, "2.3", false);
		new WaitUntil(new ProjectHasErrors(SEAM_WEB_PROJECT+"23", null), TimePeriod.LONG);
		deleteProjects(true,false);
	}
	
	@Test
	public void createSeam23EarProjectTest(){
		createSeamProject(SEAM_EAR_PROJECT+"23", SEAM23_NAME, "2.3", true);
		new WaitUntil(new ProjectHasErrors(SEAM_EAR_PROJECT+"23", null), TimePeriod.LONG);
		deleteProjects(true,false);
	}
	
	@Test
	public void createSeam22WebProjectTest(){
		createSeamProject(SEAM_WEB_PROJECT+"22", SEAM22_NAME, "2.2",false);
		new WaitWhile(new ProjectHasErrors(SEAM_WEB_PROJECT+"22", ACCEPTED_ERROR_TYPE), TimePeriod.LONG);
		deleteProjects(true,true);
	}

	@Test
	public void createSeam22EarProjectTest(){
		createSeamProject(SEAM_EAR_PROJECT+"22", SEAM22_NAME, "2.2",true);
		new WaitWhile(new ProjectHasErrors(SEAM_EAR_PROJECT+"22", ACCEPTED_ERROR_TYPE), TimePeriod.LONG);
		deleteProjects(true,true);
	}

	private void createSeamProject(String projectName, String seamRuntime, String seamVersion, boolean EAR){
		SeamProjectDialog sd = new SeamProjectDialog();
		sd.addWizardPage(new SeamProjectFifthPage(), 6);
		sd.open();
		sd.selectPage(1);
		SeamProjectFirstPage sf = (SeamProjectFirstPage)sd.getWizardPage();
		sf.setProjectName(projectName);
		sf.setRuntime(runtimeName);
		sf.setServer(serverName);
		sf.activateFacet("Seam", seamVersion);
		sf.activateFacet("JBoss Maven Integration", null);
		sd.selectPage(6);
		SeamProjectFifthPage sfp = (SeamProjectFifthPage)sd.getWizardPage();
		sfp.setSeamRuntime(seamRuntime);
		sfp.toggleEAR(EAR);
		sd.finish();
	}
	
	protected static void createDriver(String jbossASLocation,String driverLocation) throws ConnectionProfileException,IOException {
		if (ProfileManager.getInstance().getProfileByName(CONNECTION_PROFILE_NAME) != null) {
			return;
		}
		String driverPath = new File(jbossASLocation + driverLocation).getCanonicalPath(); //$NON-NLS-1$

		DriverInstance driver = DriverManager.getInstance().getDriverInstanceByName(HSQL_DRIVER_NAME);
		if (driver == null) {
			TemplateDescriptor descr = TemplateDescriptor.getDriverTemplateDescriptor(HSQL_DRIVER_TEMPLATE_ID);
			IPropertySet instance = new PropertySetImpl(HSQL_DRIVER_NAME, HSQL_DRIVER_DEFINITION_ID);
			instance.setName(HSQL_DRIVER_NAME);
			instance.setID(HSQL_DRIVER_DEFINITION_ID);
			Properties props = new Properties();

			IConfigurationElement[] template = descr.getProperties();
			for (int i = 0; i < template.length; i++) {
				IConfigurationElement prop = template[i];
				String id = prop.getAttribute("id"); //$NON-NLS-1$

				String value = prop.getAttribute("value"); //$NON-NLS-1$
				props.setProperty(id, value == null ? "" : value); //$NON-NLS-1$
			}
			props.setProperty(DTP_DB_URL_PROPERTY_ID, "jdbc:hsqldb:."); //$NON-NLS-1$
			props.setProperty(IDriverMgmtConstants.PROP_DEFN_TYPE,
					descr.getId());
			props.setProperty(IDriverMgmtConstants.PROP_DEFN_JARLIST,
					driverPath);

			instance.setBaseProperties(props);
			DriverManager.getInstance().removeDriverInstance(instance.getID());
			System.gc();
			DriverManager.getInstance().addDriverInstance(instance);
		}

		driver = DriverManager.getInstance().getDriverInstanceByName(HSQL_DRIVER_NAME);
		if (driver != null && ProfileManager.getInstance().getProfileByName(CONNECTION_PROFILE_NAME) == null) {
			// create profile
			Properties props = new Properties();
			props.setProperty(ConnectionProfileConstants.PROP_DRIVER_DEFINITION_ID,	HSQL_DRIVER_DEFINITION_ID);
			props.setProperty(IDBConnectionProfileConstants.CONNECTION_PROPERTIES_PROP_ID,""); 
			props.setProperty(IDBDriverDefinitionConstants.DRIVER_CLASS_PROP_ID,driver.getProperty(IDBDriverDefinitionConstants.DRIVER_CLASS_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.DATABASE_VENDOR_PROP_ID,driver.getProperty(IDBDriverDefinitionConstants.DATABASE_VENDOR_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.DATABASE_VERSION_PROP_ID,driver.getProperty(IDBDriverDefinitionConstants.DATABASE_VERSION_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.DATABASE_NAME_PROP_ID,"Default");
			props.setProperty(IDBDriverDefinitionConstants.PASSWORD_PROP_ID, "");
			props.setProperty(IDBConnectionProfileConstants.SAVE_PASSWORD_PROP_ID,"false");
			props.setProperty(IDBDriverDefinitionConstants.USERNAME_PROP_ID,driver.getProperty(IDBDriverDefinitionConstants.USERNAME_PROP_ID));
			props.setProperty(IDBDriverDefinitionConstants.URL_PROP_ID, driver.getProperty(IDBDriverDefinitionConstants.URL_PROP_ID));
			ProfileManager.getInstance().createProfile(CONNECTION_PROFILE_NAME,"The JBoss AS Hypersonic embedded database", HSQL_PROFILE_ID, props, "", false);
		}

	}
	
}