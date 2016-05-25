package org.jboss.tools.hibernate.reddeer.test;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.tools.hibernate.reddeer.console.EditConfigurationMainPage;
import org.jboss.tools.hibernate.reddeer.console.EditConfigurationMainPage.PredefinedConnection;
import org.jboss.tools.hibernate.reddeer.console.EditConfigurationShell;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationLocationPage;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationSettingPage;
import org.jboss.tools.hibernate.reddeer.console.NewHibernateConfigurationWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Console configuration test
 * Creates Hibernate Configuration file (cfg.xml)
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class ConsoleConfigurationTest extends HibernateRedDeerTest {

	private String PROJECT_NAME = "consoletest";
	protected  Map<String,String> libraries = new HashMap<String, String>() {{
	    put("hsqldb-2.3.4.jar",null);
	}};
	private String HIBERNATE_CFG_FILE="/" + PROJECT_NAME + "/src/hibernate.cfg.xml";
	private String CONSOLE_NAME="hibernateconsoletest";
	
	private static final Logger log = Logger.getLogger(ConsoleConfigurationTest.class);
	
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;

	
	@Before 
	public void prepare() {
		log.step("Import test project");
		importProject(PROJECT_NAME,libraries);			
	}
	
	@After 
	public void clean() {
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		v.deleteConsoleConfiguration(CONSOLE_NAME);
		
		deleteAllProjects();
	}
	
	@Test 
	public void testConsoleConfiguration35() {
		createConsoleConfiguration("3.5");
	}

	@Test 
	public void testConsoleConfiguration36() {
		createConsoleConfiguration("3.6");
	}

	@Test 
	public void testConsoleConfiguration40() {
		createConsoleConfiguration("4.0");
	}

	@Test 
	public void testConsoleConfiguration43() {
		createConsoleConfiguration("4.3");
	}
	
	@Test 
	public void testConsoleConfiguration50() {
		createConsoleConfiguration("5.0");
	}
	
	private void createConsoleConfiguration(String hibernateVersion) {
		prepareConsoleConfigurationFile(hibernateVersion);
		prepareConsoleConfiguration(hibernateVersion);
	}
	
	public void prepareConsoleConfigurationFile(String hibernateVersion) {		
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		
		log.step("Open Hibernate Configuration File wizard");
		NewHibernateConfigurationWizard wizard = new NewHibernateConfigurationWizard();
		wizard.open();
		log.step("Set hibernate configuration values");
		NewConfigurationLocationPage p1 = new NewConfigurationLocationPage();
		p1.setLocation(PROJECT_NAME,"src");
		wizard.next();

		NewConfigurationSettingPage p2 = new NewConfigurationSettingPage();
		p2.setDatabaseDialect("H2");
		p2.setDriverClass(cfg.getDriverClass());
		p2.setConnectionURL(cfg.getJdbcString());
		p2.setUsername(cfg.getUsername());
		p2.setHibernateVersion(hibernateVersion);
		log.step("Finish");
		wizard.finish();
	}

	public void prepareConsoleConfiguration(String hibernateVersion) {
		log.step("Open Hibernate Console Configuration view");
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		log.step("Add new configuration");
		v.triggerAddConfigurationDialog();
		
		EditConfigurationShell s = new EditConfigurationShell();
		s.setName(CONSOLE_NAME);		
				
		EditConfigurationMainPage p = s.getMainPage();		
				
		p.setProject(PROJECT_NAME);
		p.setDatabaseConnection(PredefinedConnection.JPA_PROJECT_CONFIGURED_CONNECTION);
		p.setDatabaseConnection(PredefinedConnection.HIBERNATE_CONFIGURED_CONNECTION);		
		p.setConfigurationFile(HIBERNATE_CFG_FILE);
		p.setHibernateVersion(hibernateVersion);
		//ANY ERROR IN WIZARD ??
		log.step("Press OK");
		s.ok();
		
		v.open();
		EditConfigurationShell s2 = v.openConsoleConfiguration(CONSOLE_NAME);
		s2.close();
		
		v.open();
		v.selectNode(CONSOLE_NAME,"Database","SAKILA.PUBLIC","ACTOR"); //TODO FIX THIS - SEE INSIDE
	}
}
