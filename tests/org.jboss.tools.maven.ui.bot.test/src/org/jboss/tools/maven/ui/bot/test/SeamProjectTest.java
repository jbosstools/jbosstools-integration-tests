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

import org.eclipse.core.runtime.CoreException;
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
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.ShellTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.seam.core.project.facet.SeamRuntime;
import org.jboss.tools.seam.core.project.facet.SeamRuntimeManager;
import org.jboss.tools.seam.core.project.facet.SeamVersion;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
public class SeamProjectTest extends AbstractMavenSWTBotTest {

	public static final String SEAM_WEB_PROJECT = "seamWeb";
	public static final String SEAM_EAR_PROJECT = "seamEar";
	public static final String JBOSS_AS_7_1 = System.getProperty("jbosstools.test.jboss.home.7.1");
	public static final String SEAM_2_3 = System.getProperty("jbosstools.test.seam.2.3.0.home");
	public static final String SEAM_2_3_NAME = "jboss-seam-2.3.0";
	public static final String SEAM_2_2 = System.getProperty("jbosstools.test.seam.2.2.0.home");
	public static final String SEAM_2_2_NAME = "jboss-seam-2.2.0";
	public static final String CONNECTION_PROFILE_NAME = "DefaultDS";
	public static final String HSQL_DRIVER_DEFINITION_ID ="DriverDefn.Hypersonic DB";
	public static final String HSQL_DRIVER_NAME ="Hypersonic DB";
	public static final String HSQL_DRIVER_TEMPLATE_ID = "org.eclipse.datatools.enablement.hsqldb.1_8.driver";
	public static final String DTP_DB_URL_PROPERTY_ID = "org.eclipse.datatools.connectivity.db.URL";
	public static final String HSQL_PROFILE_ID = "org.eclipse.datatools.enablement.hsqldb.connectionProfile";
	public static final String HSQLDB_DRIVER_LOCATION =System.getProperty("hsqldb.driver");
	public static final String CURRENT_SEAM_2_3 = "2.3.0.Final";
	public static final String CURRENT_SEAM_2_2 ="2.2.2.Final";
	
			
	private SWTUtilExt botUtil = new SWTUtilExt(bot);

	@BeforeClass
	public static void setup() {
		setPerspective("Seam");
	}
	
	@Test
	public void createSeam23WebProjectTest() throws InterruptedException, ConnectionProfileException, IOException, CoreException{
		createSeamProject(SEAM_WEB_PROJECT,"2.3", "WAR", "Disable Library Configuration");
		//checkErrors(); TODO QuickFix Project
		deleteProjects(true);
	}
	
	@Test
	public void createSeam23EarProjectTest() throws InterruptedException, ConnectionProfileException, IOException, CoreException{
		createSeamProject(SEAM_EAR_PROJECT,"2.3", "EAR", "Disable Library Configuration");
		//checkErrors(); TODO QuickFix Project
		deleteProjects(true);
	}
	
	@Test
	public void createSeam22WebProjectTest() throws InterruptedException, ConnectionProfileException, IOException, CoreException{
		createSeamProject(SEAM_WEB_PROJECT,"2.2", "WAR", "Disable Library Configuration");
		//checkErrors();
		deleteProjects(true);
	}
	
	@Test
	public void createSeam22EarProjectTest() throws InterruptedException, ConnectionProfileException, IOException, CoreException{
		createSeamProject(SEAM_EAR_PROJECT,"2.2", "EAR", "Disable Library Configuration");
		//checkErrors();
		deleteProjects(true);
	}
	
	private void createSeamProject(String name, String version, String type, String JSFLibrary) throws InterruptedException, ConnectionProfileException, IOException, CoreException {
		createDriver(JBOSS_AS_7_1, HSQLDB_DRIVER_LOCATION);
		if(version.equals("2.3")){
			createSeamRuntime(SEAM_2_3_NAME, SEAM_2_3, SeamVersion.SEAM_2_3);
		} else if(version.equals("2.2")){
			createSeamRuntime(SEAM_2_2_NAME, SEAM_2_2, SeamVersion.SEAM_2_2);
		}
		new ShellMenu("File","New","Seam Web Project").select();
		new WaitUntil(new ShellWithTextIsActive("New Seam Project"), TimePeriod.NORMAL);
		new LabeledText("Project name:").setText(name);
		new PushButton("New Runtime...").click();
		new WaitUntil(new ShellWithTextIsActive("New Server Runtime Environment"), TimePeriod.NORMAL);
		new ShellTreeItem("JBoss Community", "JBoss 7.1 Runtime").select();
		new PushButton("Next >").click();
		new LabeledText("Home Directory").setText(JBOSS_AS_7_1);
		new PushButton("Finish").click();
		new PushButton("New...").click();
		new WaitUntil(new ShellWithTextIsActive("New Server"), TimePeriod.NORMAL);
		new ShellTreeItem("JBoss Community", "JBoss AS 7.1").select();
		new PushButton("Finish").click();
		new PushButton("Modify...").click();
		new WaitUntil(new ShellWithTextIsActive("Project Facets"), TimePeriod.NORMAL);
		new ShellTreeItem("Seam").select();
		new ContextMenu("Change Version...").select();
		new WaitUntil(new ShellWithTextIsActive("Change Version"), TimePeriod.NORMAL);
		new DefaultCombo("Version:").setSelection(version);
		new PushButton("OK").click();
		new ShellTreeItem("JBoss Maven Integration").setChecked(true);
		new PushButton("OK").click();
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		assertTrue("Seam project doesn't have war packaging set by default", new DefaultCombo("Packaging:").getSelection().equals("war"));
		String seamVersion = new LabeledText("Seam Maven version:").getText();
		if(version.equals("2.3")){
			assertTrue(version+ " Seam project has " + seamVersion + " set by default", seamVersion.equals(CURRENT_SEAM_2_3));
		} else if(version.equals("2.2")){
			assertTrue(version+ " Seam project has " + seamVersion + " set by default", seamVersion.equals(CURRENT_SEAM_2_2));
		}
		new PushButton("Next >").click();
		new DefaultCombo("Type:").setSelection("Disable Library Configuration");
		new PushButton("Next >").click();
		if(version.equals("2.3")){
			new DefaultCombo(0).setSelection(SEAM_2_3_NAME);
		} else if(version.equals("2.2")){
			new DefaultCombo(0).setSelection(SEAM_2_2_NAME);
		}
		bot.radio(type).click();
		new PushButton("Finish").click();
		botUtil.waitForAll(Long.MAX_VALUE);
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
	

	protected static void createSeamRuntime(String name, String seamPath, SeamVersion seamVersion) {
		SeamRuntime seamRuntime = SeamRuntimeManager.getInstance().findRuntimeByName(name);
		if (seamRuntime != null) {
			return;
		}
		File seamFolder = new File(seamPath);
		if(seamFolder.exists() && seamFolder.isDirectory()) {
			SeamRuntime rt = new SeamRuntime();
			rt.setHomeDir(seamPath);
			rt.setName(name);
			rt.setDefault(true);
			rt.setVersion(seamVersion);
			SeamRuntimeManager.getInstance().addRuntime(rt);
		} else {
			fail("Invalid seam runtime.");
		}
	}
	
}