package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
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
	
	public void testCreateConfigurationFile(boolean generateConsole) {		
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		
		NewHibernateConfigurationWizard wizard = new NewHibernateConfigurationWizard();
		wizard.open();
		NewConfigurationLocationPage p1 = (NewConfigurationLocationPage)wizard.getFirstPage();
		p1.setLocation(PROJECT_NAME,"src");		
		wizard.next();

		NewConfigurationSettingPage p2 = (NewConfigurationSettingPage)wizard.getWizardPage();
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
