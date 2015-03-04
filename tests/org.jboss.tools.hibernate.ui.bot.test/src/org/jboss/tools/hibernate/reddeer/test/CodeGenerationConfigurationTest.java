package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.impl.tree.TreeItemNotFoundException;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.dialog.LaunchConfigurationsDialog;
import org.jboss.tools.hibernate.reddeer.editor.RevengEditor;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.wizard.NewReverseEngineeringFileWizard;
import org.jboss.tools.hibernate.reddeer.wizard.TableFilterWizardPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test prepares project and generate entities via Hibernate Code Generation Configuration 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class CodeGenerationConfigurationTest extends HibernateRedDeerTest {

	private final String PRJ = "mvn-hibernate43"; 
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
    @Before
	public void testConnectionProfile() {
    	importProject(PRJ);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		HibernateToolsFactory.testCreateConfigurationFile(cfg, PRJ, "hibernate.cfg.xml", true);
	}
    
    @Test
    public void testHibernateGenerateConfiguration() {
    	createHibernateGenerationConfiguration(false);
    	checkGeneratedEntities();
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng() {
    	createRevengFile();
    	createHibernateGenerationConfiguration(true);
    	checkGeneratedEntities();
    }
    
    private void createHibernateGenerationConfiguration(boolean reveng) {

    	LaunchConfigurationsDialog dlg = new LaunchConfigurationsDialog();
    	dlg.open();
    	dlg.createNewConfiguration();
    	dlg.selectConfiguration(PRJ);
    	dlg.setOutputDir("/" + PRJ + "/src/main/java");
    	dlg.setPackage("org.gen");
    	dlg.setReverseFromJDBC(true);
    	if (reveng) dlg.setRevengFile(PRJ,"hibernate.reveng.xml");
    	dlg.selectExporter(0);
    	dlg.selectExporter(1);
    	dlg.apply();
    	dlg.run();
    }
    	    	
    private void checkGeneratedEntities() {
    	PackageExplorer pe = new PackageExplorer();    
    	pe.open();    	
    	try {
    		// need to wait here to get treeitem ready, TODO: implement proper condition
    		AbstractWait.sleep(TimePeriod.NORMAL);
    		new DefaultTreeItem(PRJ,"src/main/java","org.gen","Actor.java").doubleClick();
    	}
    	catch (TreeItemNotFoundException e) {
    		fail("Entities not generated, possible cause https://issues.jboss.org/browse/JBIDE-19217");
    	}
    	new DefaultEditor("Actor.java");
    }

	private void createRevengFile() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PRJ);		
		
		NewReverseEngineeringFileWizard wizard = new NewReverseEngineeringFileWizard();
		wizard.open();
		wizard.next();
		TableFilterWizardPage page = new TableFilterWizardPage();
		page.setConsoleConfiguration(PRJ);
		page.refreshDatabaseSchema();
		page.pressInclude();
		wizard.finish();

		EditorHandler.getInstance().closeAll(false);
		pe.open();
		new DefaultTreeItem(PRJ,"hibernate.reveng.xml").doubleClick();
		new DefaultEditor("Hibernate Reverse Engineering Editor").activate();
		
		RevengEditor re = new RevengEditor();
		re.activateDesignTab();
		re.activateOverviewTab();
		re.activateTableFiltersTab();
		re.activateTypeMappingsTab();
		re.activateTableAndColumnsTab();
		re.selectAllTables();
		re.activateSourceTab();
	}

	@After
	public void cleanUp() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PRJ).delete(true);
	}
}