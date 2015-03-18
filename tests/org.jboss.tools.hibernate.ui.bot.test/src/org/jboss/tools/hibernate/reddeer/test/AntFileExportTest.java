package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.interceptor.SyncInterceptorManager;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.interceptor.ErrorLogInterceptor;
import org.jboss.tools.hibernate.reddeer.perspective.HibernatePerspective;
import org.jboss.tools.hibernate.reddeer.wizard.ExportAntCodeGenWizard;
import org.jboss.tools.hibernate.reddeer.wizard.ExportAntCodeGenWizardPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test export ant file based on Hibernate Code Generation Configuration 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class AntFileExportTest extends HibernateRedDeerTest {

	private final String PRJ = "antconfiguration";
	private final String GEN_NAME = "genconfiguration";
	private final String ANTFILE_NAME = "build.xml";
	private final Logger log = Logger.getLogger(this.getClass());
	

    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
    @Before
	public void testConnectionProfile() {
    	DatabaseConfiguration cfg = dbRequirement.getConfiguration();
    	
    	log.step("Import project with hibernate configuration and hibernate code generation configuration");
    	importProject(PRJ);		
		
    	log.step("Create configuration file for the project");
		HibernateToolsFactory.testCreateConfigurationFile(cfg, PRJ, "hibernate.cfg.xml", true);
	}
    
    @Test
    public void testAntFilenameExport() {
    	
    	HibernatePerspective p = new HibernatePerspective();
    	p.open();
    	
    	new ShellMenu("Run", "Hibernate Code Generation...","Hibernate Code Generation Configurations...").select();
    	new WaitUntil(new ShellWithTextIsActive("Hibernate Code Generation Configurations"));
    	new DefaultTreeItem("Hibernate Code Generation",GEN_NAME).select();
    	new LabeledCombo("Console configuration:").setSelection(PRJ);
    	
    	new PushButton("Apply").click();
    	new PushButton("Close").click();
    	
    	new WaitWhile(new ShellWithTextIsActive("Hibernate Code Generation Configurations"));
    	new WaitWhile(new JobIsRunning());
    	    	
    	PackageExplorer pe = new PackageExplorer();    
    	pe.open();
    	pe.selectProjects(PRJ);
    	
    	log.step("Open export wizard (Export ->  Hibernate -> Ant Code Generation)");
    	ExportAntCodeGenWizard w = new ExportAntCodeGenWizard();
    	w.open();
    	ExportAntCodeGenWizardPage page = new ExportAntCodeGenWizardPage();
    	page.setHibernateGenConfiguration(GEN_NAME);
    	page.setAntFileName(ANTFILE_NAME);
    	
    	log.step("Click finish to export ant file");
    	w.finish();
    	new WaitWhile(new JobIsRunning());  	
    	
    	log.step("Open the exported ant file");
    	pe.open();
    	new DefaultTreeItem(PRJ,ANTFILE_NAME).doubleClick();
    	
    	assertTrue("Ant file cannot be ampty", new TextEditor(ANTFILE_NAME).getText().length() > 0);
    }
    
	@After
	public void cleanUp() {
	}
}