package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
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

    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
	private String prj = "mvn-hibernate43-ent"; 
	private String hbVersion = "4.3";

	private void prepare() {
    	importProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		HibernateToolsFactory.testCreateConfigurationFile(cfg, prj, "hibernate.cfg.xml", true);
		HibernateToolsFactory.setHibernateVersion(prj, hbVersion);
	}
    
    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    }
    
    @Test
    public void testHibernateGenerateConfiguration35() {
    	setParams("mvn-hibernate35","3.5","2.0");
    	createHibernateGenerationConfiguration(true);
    }
    
    @Test
    public void testHibernateGenerateConfiguration36() {
    	setParams("mvn-hibernate36","3.6","2.0");
    	createHibernateGenerationConfiguration(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration40() {
    	setParams("mvn-hibernate40","4.0","2.0");
    	createHibernateGenerationConfiguration(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration43() {
    	setParams("mvn-hibernate43","4.3","2.1");
    	createHibernateGenerationConfiguration(false);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng35() {
    	setParams("mvn-hibernate35","3.5","2.0");
    	createHibernateGenerationConfiguration(true);
    }
        
    @Test
    public void testHibernateGenerateConfigurationWithReveng36() {
    	setParams("mvn-hibernate36","3.6","2.0");
    	createHibernateGenerationConfiguration(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng40() {
    	setParams("mvn-hibernate40","4.0","2.0");
    	createHibernateGenerationConfiguration(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng43() {
    	setParams("mvn-hibernate43","4.3","2.1");
    	createHibernateGenerationConfiguration(true);
    }
    
    private void createHibernateGenerationConfiguration(boolean reveng) {

    	prepare();
    	
    	if (reveng) {
    		createRevengFile();
    	}
    	
    	LaunchConfigurationsDialog dlg = new LaunchConfigurationsDialog();
    	dlg.open();
    	dlg.createNewConfiguration();
    	dlg.selectConfiguration(prj);
    	dlg.setOutputDir("/" + prj + "/src/main/java");
    	dlg.setPackage("org.gen");
    	dlg.setReverseFromJDBC(true);
    	if (reveng) dlg.setRevengFile(prj,"hibernate.reveng.xml");
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
    		new DefaultTreeItem(prj,"src/main/java","org.gen","Actor.java").doubleClick();
    	}
    	catch (TreeItemNotFoundException e) {
    		fail("Entities not generated, possible cause https://issues.jboss.org/browse/JBIDE-19217");
    	}
    	new DefaultEditor("Actor.java");
    }

	private void createRevengFile() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(prj);		
		
		NewReverseEngineeringFileWizard wizard = new NewReverseEngineeringFileWizard();
		wizard.open();
		wizard.next();
		TableFilterWizardPage page = new TableFilterWizardPage();
		page.setConsoleConfiguration(prj);
		page.refreshDatabaseSchema();
		page.pressInclude();
		wizard.finish();

		EditorHandler.getInstance().closeAll(false);
		pe.open();
		new DefaultTreeItem(prj,"hibernate.reveng.xml").doubleClick();
		new DefaultEditor("Hibernate Reverse Engineering Editor").activate();
		
		RevengEditor re = new RevengEditor();
		re.activateDesignTab();
		re.activateOverviewTab();
		re.activateTableFiltersTab();
		re.activateTypeMappingsTab();
		re.activateTableAndColumnsTab();
		try {
			re.selectAllTables();
		} catch (WaitTimeoutExpiredException e) {
			fail("Cannot add tables - known issue(s) - JBIDE-19443");
		}
		re.activateSourceTab();
	}

	@After
	public void cleanUp() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(prj).delete(true);
	}
}