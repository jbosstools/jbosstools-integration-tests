package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.api.Link;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationLocationPage;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationSettingPage;
import org.jboss.tools.hibernate.reddeer.console.NewHibernateConfigurationWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class ConsoleConfigurationFileTest extends HibernateRedDeerTest {

	private String PROJECT_NAME = "configurationtest";
	private String HIBERNATE_CFG_FILE="hibernate.cfg.xml";
	
    @InjectRequirement
    private DatabaseRequirement dbRequirement;

	
	@Before 
	public void prepare() {
		importProject(PROJECT_NAME);
	}
	
	@Test
	public void testCreateConfigurationFileWithoutConsole() {		
		testCreateConfigurationFile(false);
	}

	@Test
	public void testCreateConfigurationFileWithConsole() {
		testCreateConfigurationFile(true);
	}
	
	@Test
	public void testCreateConfigurationFileFromDatasource() {
		// Create datasource
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDefinition(cfg);
		ConnectionProfileFactory.createConnectionProfile(cfg);

		NewHibernateConfigurationWizard wizard = new NewHibernateConfigurationWizard();
		wizard.open();
		
		NewConfigurationLocationPage p1 = new NewConfigurationLocationPage();
		p1.setLocation(PROJECT_NAME,"src");		
		wizard.next();

		// Get values from connection
		Link link = new DefaultLink("Get values from Connection");
		link.click();
		new WaitUntil(new ShellWithTextIsActive("Select Connection Profile"));
		new DefaultCombo().setSelection(cfg.getProfileName());
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive("Select Connection Profile"));

		// Check values
		NewConfigurationSettingPage p2 = new NewConfigurationSettingPage();
		assertTrue("jdbc must match", p2.getConnectionURL().equals(cfg.getJdbcString()));
		assertTrue("driver must match", p2.getDriveClass().equals(cfg.getDriverClass()));
		assertTrue("username must match", p2.getUsername().equals(cfg.getUsername()));
		
		new CancelButton().click();

	}
		
	public void testCreateConfigurationFile(boolean generateConsole) {		
		NewHibernateConfigurationWizard wizard = new NewHibernateConfigurationWizard();
		wizard.open();
		NewConfigurationLocationPage p1 = new NewConfigurationLocationPage();
		p1.setLocation(PROJECT_NAME,"src");		
		wizard.next();

		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		NewConfigurationSettingPage p2 = new NewConfigurationSettingPage();
		p2.setConnectionURL(cfg.getJdbcString());
		p2.setUsername(cfg.getUsername());		
		
		if (generateConsole) {
			p2.setCreateConsoleConfiguration(generateConsole);
		}
		
		wizard.finish();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		DefaultTreeItem ti = new DefaultTreeItem(PROJECT_NAME,"src",HIBERNATE_CFG_FILE);
		ti.doubleClick();
		new DefaultEditor(HIBERNATE_CFG_FILE);
		
		if (generateConsole) {
			KnownConfigurationsView v = new KnownConfigurationsView();
			v.open();		
		}
	}
			
	@After 
	public void clean() {			
		ProjectExplorer pe = new ProjectExplorer();
		pe.getProject(PROJECT_NAME).delete(true);
	}

}
