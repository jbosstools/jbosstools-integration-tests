package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationLocationPage;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationSettingPage;
import org.jboss.tools.hibernate.reddeer.console.NewHibernateConfigurationWizard;
import org.jboss.tools.hibernate.reddeer.editor.RevengEditor;
import org.jboss.tools.hibernate.reddeer.wizard.NewReverseEngineeringFileWizard;
import org.jboss.tools.hibernate.reddeer.wizard.TableFilterWizardPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Reverse Engineering File (reveng.xml) file test
 * Creates file
 * @author jpeterka
 *
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class RevengFileTest extends HibernateRedDeerTest {

	private String PROJECT_NAME = "revengfiletest";
	@InjectRequirement
    private DatabaseRequirement dbRequirement;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@Before 
	public void prepare() {
		log.step("Import test project");
		importProject(PROJECT_NAME);
		prepareConsoleConfiguration();
	}
	
	public void prepareConsoleConfiguration() {
		log.step("Create hibernate configuration file with console configuration");
		NewHibernateConfigurationWizard wizard = new NewHibernateConfigurationWizard();
		wizard.open();
		NewConfigurationLocationPage p1 = new NewConfigurationLocationPage();
		p1.setLocation(PROJECT_NAME,"src");		
		wizard.next();

		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		NewConfigurationSettingPage p2 = new NewConfigurationSettingPage();
		p2.setDatabaseDialect("H2");
		p2.setDriverClass(cfg.getDriverClass());
		p2.setConnectionURL(cfg.getJdbcString());
		p2.setUsername(cfg.getUsername());		
		p2.setCreateConsoleConfiguration(true);
		
		wizard.finish();
	}
	
	@Test
	public void testCreateRevengFile() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PROJECT_NAME);		
		
		log.step("Create hibernate reverese engineering via reveng wizard");
		NewReverseEngineeringFileWizard wizard = new NewReverseEngineeringFileWizard();
		wizard.open();
		wizard.next();
		TableFilterWizardPage page = new TableFilterWizardPage();
		page.setConsoleConfiguration(PROJECT_NAME);
		page.refreshDatabaseSchema();
		page.pressInclude();
		log.step("Finish wizard to create a file");
		wizard.finish();

		EditorHandler.getInstance().closeAll(false);
		pe.open();
		new DefaultTreeItem(PROJECT_NAME,"hibernate.reveng.xml").doubleClick();
		new DefaultEditor("Hibernate Reverse Engineering Editor").activate();
		
		RevengEditor re = new RevengEditor();
		re.activateDesignTab();
		re.activateOverviewTab();
		re.activateSourceTab();
		re.activateTableFiltersTab();
		re.activateTypeMappingsTab();		
	}

	@After 
	public void clean() {			
	}
}
