package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.api.Link;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationLocationPage;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationSettingPage;
import org.jboss.tools.hibernate.reddeer.console.NewHibernateConfigurationWizard;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class ConsoleConfigurationFileTest extends HibernateRedDeerTest {

	private String PROJECT_NAME = "configurationtest";
	protected  Map<String,String> libraries = new HashMap<String, String>() {{
	    put("hsqldb-2.3.4.jar",null);
	}};
	//,"postgresql-9.4.1208.jre7.jar", "mysql-connector-java-6.0.2.jar"
	private String HIBERNATE_CFG_FILE="hibernate.cfg.xml";
	private static final Logger log = Logger.getLogger(ConsoleConfigurationFileTest.class);
	
    @InjectRequirement
    private DatabaseRequirement dbRequirement;

	
	@Before 
	public void prepare() {
		importProject(PROJECT_NAME, libraries);
	}
	
	@After 
	public void clean() {			
		deleteAllProjects();
	}
	
	@Test
	public void testCreateConfigurationFileWithoutConsole35() {		
		testCreateConfigurationFile("3.5",false);
	}

	@Test
	public void testCreateConfigurationFileWithConsole35() {
		testCreateConfigurationFile("3.5",true);
	}
			
	@Test
	public void testCreateConfigurationFileFromDatasource35() {
		createConfigurationFileFromDatasource("3.5");
	}
	
	@Test
	public void testCreateConfigurationFileWithoutConsole36() {		
		testCreateConfigurationFile("3.6",false);
	}

	@Test
	public void testCreateConfigurationFileWithConsole36() {
		testCreateConfigurationFile("3.6",true);
	}
			
	@Test
	public void testCreateConfigurationFileFromDatasource36() {
		createConfigurationFileFromDatasource("3.6");
	}
	
	@Test
	public void testCreateConfigurationFileWithoutConsole40() {		
		testCreateConfigurationFile("4.0",false);
	}

	@Test
	public void testCreateConfigurationFileWithConsole40() {
		testCreateConfigurationFile("4.0",true);
	}
			
	@Test
	public void testCreateConfigurationFileFromDatasource40() {
		createConfigurationFileFromDatasource("4.0");
	}
	
	@Test
	public void testCreateConfigurationFileWithoutConsole43() {		
		testCreateConfigurationFile("4.3",false);
	}

	@Test
	public void testCreateConfigurationFileWithConsole43() {
		testCreateConfigurationFile("4.3",true);
	}
			
	@Test
	public void testCreateConfigurationFileFromDatasource43() {
		createConfigurationFileFromDatasource("4.3");
	}
	
	@Test
	public void testCreateConfigurationFileWithoutConsole50() {		
		testCreateConfigurationFile("5.0",false);
	}
	
	@Test
	public void testCreateConfigurationFileWithoutConsole51() {		
		testCreateConfigurationFile("5.1",false);
	}
	
	@Test
	public void testCreateConfigurationFileWithoutConsole52() {		
		testCreateConfigurationFile("5.2",false);
	}

	@Test
	public void testCreateConfigurationFileWithConsole50() {
		testCreateConfigurationFile("5.0",true);
	}
	
	@Test
	public void testCreateConfigurationFileWithConsole51() {
		testCreateConfigurationFile("5.1",true);
	}
	
	@Test
	public void testCreateConfigurationFileWithConsole52() {
		testCreateConfigurationFile("5.2",true);
	}
	
	@Test
	public void testCreateConfigurationFileFromDatasource50() {
		createConfigurationFileFromDatasource("5.0");
	}
	
	@Test
	public void testCreateConfigurationFileFromDatasource51() {
		createConfigurationFileFromDatasource("5.1");
	}
	
	@Test
	public void testCreateConfigurationFileFromDatasource52() {
		createConfigurationFileFromDatasource("5.2");
	}
	
	
	private void createConfigurationFileFromDatasource(String hbVersion) {
		// Create datasource
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create database connection profile");
		ConnectionProfileFactory.createConnectionProfile(cfg);

		log.step("Create Hibernate configuration file");
		NewHibernateConfigurationWizard wizard = new NewHibernateConfigurationWizard();
		wizard.open();
		
		NewConfigurationLocationPage p1 = new NewConfigurationLocationPage();		
		p1.setLocation(PROJECT_NAME,"src");		
		wizard.next();

		// Get values from connection
		log.step("Use created database connection profile for database details");
		Link link = new DefaultLink("Get values from Connection");
		link.click();
		new WaitUntil(new ShellWithTextIsAvailable("Select Connection Profile"));
		new DefaultCombo().setSelection(cfg.getProfileName());
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Select Connection Profile"));

		new DefaultShell("");
		
		// Check values
		NewConfigurationSettingPage p2 = new NewConfigurationSettingPage();
		p2.setHibernateVersion(hbVersion);
		assertTrue("jdbc must match", p2.getConnectionURL().equals(cfg.getJdbcString()));
		assertTrue("driver must match", p2.getDriveClass().equals(cfg.getDriverClass()));
		assertTrue("username must match", p2.getUsername().equals(cfg.getUsername()));
		
		log.step("Finish the wizard");
		wizard.finish();
		
		checkFile(false);
	}
		
	public void testCreateConfigurationFile(String hbVersion, boolean generateConsole) {
		
		log.step("Create Hibernate configuration file");
		NewHibernateConfigurationWizard wizard = new NewHibernateConfigurationWizard();
		wizard.open();
		NewConfigurationLocationPage p1 = new NewConfigurationLocationPage();
		p1.setLocation(PROJECT_NAME,"src");		
		wizard.next();

		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		NewConfigurationSettingPage p2 = new NewConfigurationSettingPage();
		p2.setHibernateVersion(hbVersion);
		p2.setConnectionURL(cfg.getJdbcString());
		p2.setUsername(cfg.getUsername());		
		
		if (generateConsole) {
			p2.setCreateConsoleConfiguration(generateConsole);
		}
		log.step("Finish the wizard");
		wizard.finish();
		
		checkFile(generateConsole);
	}
	
	private void checkFile(boolean generateConsole) {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		DefaultTreeItem ti = new DefaultTreeItem(PROJECT_NAME,"src",HIBERNATE_CFG_FILE);
		ti.doubleClick();
		new DefaultEditor(HIBERNATE_CFG_FILE);
		
		if (generateConsole) {
			KnownConfigurationsView v = new KnownConfigurationsView();
			v.selectConsole(PROJECT_NAME);		
		}
	}

}
