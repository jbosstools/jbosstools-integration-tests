package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
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
	private String PROJECT_LIBS = "hibernatelib"; 
	private String HIBERNATE_CFG_FILE="/" + PROJECT_NAME + "/src/hibernate.cfg.xml";
	private String CONSOLE_NAME="hibernateconsoletest";
	
	private Logger log = Logger.getLogger(this.getClass());
	
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;

	
	@Before 
	public void prepare() {
		log.step("Import test project");
		importProject(PROJECT_LIBS);
		importProject(PROJECT_NAME);		
		prepareConsoleConfiguration();
	}
	
	public void prepareConsoleConfiguration() {		
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
		log.step("Finish");
		wizard.finish();
	}

	@Test
	public void testCreateConsoleConfiguration() {
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
		
		log.step("Press OK");
		s.ok();
		
		v.open();
		EditConfigurationShell s2 = v.openConsoleConfiguration(CONSOLE_NAME);
		s2.close();
		
		v.open();
		v.selectNode(CONSOLE_NAME,"Database","SAKILA.PUBLIC","ACTOR");
	}
	
	

	@After 
	public void clean() {			
		ProjectExplorer pe = new ProjectExplorer();
		pe.getProject(PROJECT_NAME).delete(true);
	}
}
