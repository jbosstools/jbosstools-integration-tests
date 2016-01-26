package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.common.FileHelper;
import org.jboss.tools.hibernate.reddeer.dialog.LaunchConfigurationsDialog;
import org.jboss.tools.hibernate.reddeer.dialog.ProjectPropertyDialog;
import org.jboss.tools.hibernate.reddeer.editor.RevengEditor;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.wizard.NewReverseEngineeringFileWizard;
import org.jboss.tools.hibernate.reddeer.wizard.TableFilterWizardPage;
import org.jboss.tools.hibernate.ui.bot.test.Activator;
import org.junit.After;
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
	
	private static final Logger log = Logger.getLogger(CodeGenerationConfigurationTest.class);
	
	@After
	public void cleanUp() {
		deleteAllProjects();
	}

	// Mavenized projects
    @Test
    public void testHibernateGenerateConfiguration35() {
    	setParams("mvn-hibernate35","3.5","2.0");
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration36() {
    	setParams("mvn-hibernate36","3.6","2.0");
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration40() {
    	setParams("mvn-hibernate40","4.0","2.0");
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration43() {
    	setParams("mvn-hibernate43","4.3","2.1");
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration50() {
    	setParams("mvn-hibernate50","5.0","2.1");
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng35() {
    	setParams("mvn-hibernate35","3.5","2.0");
    	createHibernateGenerationConfigurationMvn(true);
    }
        
    @Test
    public void testHibernateGenerateConfigurationWithReveng36() {
    	setParams("mvn-hibernate36","3.6","2.0");
     	createHibernateGenerationConfigurationMvn(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng40() {
    	setParams("mvn-hibernate40","4.0","2.0");
    	createHibernateGenerationConfigurationMvn(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng43() {
    	setParams("mvn-hibernate43","4.3","2.1");
    	createHibernateGenerationConfigurationMvn(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng50() {
    	setParams("mvn-hibernate50","5.0","2.1");
    	createHibernateGenerationConfigurationMvn(true);
    }
    
    // Non-mavenized projects
    @Test
    public void testHibernateGenerateConfigurationEcl35() {
    	setParams("ecl-hibernate35","3.5","2.0");
    	createHibernateGenerationConfigurationEcl(false);
    }
 
    @Test
    public void testHibernateGenerateConfigurationEcl36() {
    	setParams("ecl-hibernate36","3.6","2.0");
    	createHibernateGenerationConfigurationEcl(false);
    }
    
    @Test
    public void testHibernateGenerateConfigurationEcl40() {
    	setParams("ecl-hibernate40","4.0","2.0");
    	createHibernateGenerationConfigurationEcl(false);
    }
    @Test
    public void testHibernateGenerateConfigurationRelEcl35() {
    	setParams("ecl-hibernate35","3.5","2.0");
    	createHibernateGenerationConfigurationEcl(true);
    }
 
    @Test
    public void testHibernateGenerateConfigurationRelEcl36() {
    	setParams("ecl-hibernate36","3.6","2.0");
    	createHibernateGenerationConfigurationEcl(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationRelEcl40() {
    	setParams("ecl-hibernate40","4.0","2.0");
    	createHibernateGenerationConfigurationEcl(true);
    }

    
	private void prepareMvn() {
		
		log.step("Import maven project");
    	importMavenProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Create Hibernate configuration file with Hibernate Console");
		HibernateToolsFactory.testCreateConfigurationFile(cfg, prj, "hibernate.cfg.xml", true);
		log.step("Set hibernate version in Hibernate Console");
		HibernateToolsFactory.setHibernateVersion(prj, hbVersion);
	}

	private void prepareEcl() {
		
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		String destDir = FileHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,"resources","prj","hibernatelib","connector" );
		try {
			FileHelper.copyFilesBinary(cfg.getDriverPath(), destDir);
		} catch (IOException e) {
			// Assert.fail("Cannot copy h2 driver");
		}
		log.step("Import hibernatelib and java project");
    	importProject("hibernatelib");    	
    	importProject(prj);
    	
    	log.step("Create Hibernate configuration file with Hibernate Console");
		HibernateToolsFactory.testCreateConfigurationFile(cfg, prj, "hibernate.cfg.xml", true);
		log.step("Set Hibernate Version in Hibernate Console");
		HibernateToolsFactory.setHibernateVersion(prj, hbVersion);
		log.step("Hibernate console in Hibernate Settings in Project Properties");
		setHibernateSettings();

	}

	
    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    }
    
    private void createHibernateGenerationConfigurationMvn(boolean reveng) {
    	prepareMvn();
    	createHibernateGenerationConfiguration(reveng,"src/main/java");
    }
    
    private void createHibernateGenerationConfigurationEcl(boolean reveng) {
    	prepareEcl();
     	createHibernateGenerationConfiguration(reveng, "src");
    }
    	
    private void createHibernateGenerationConfiguration(boolean reveng, String src) {    	
    	if (reveng) {
    		log.step("Create hibernate reverse engineer file");
    		createRevengFile();
    	}
    	
    	log.step("Create Hibernate Code generation configuration");
    	LaunchConfigurationsDialog dlg = new LaunchConfigurationsDialog();
    	dlg.open();
    	dlg.createNewConfiguration();
    	dlg.selectConfiguration(prj);
    	dlg.setOutputDir("/" + prj + "/" + src);
    	dlg.setPackage("org.gen");
    	dlg.setReverseFromJDBC(true);    	
    	if (reveng) dlg.setRevengFile(prj,"hibernate.reveng.xml");
    	new DefaultShell(LaunchConfigurationsDialog.DIALOG_TITLE).setFocus();
    	dlg.selectExporter(0);
    	dlg.selectExporter(1);
    	dlg.apply();
    	log.step("Click run to generate code");
    	dlg.run();
    	
    	checkGeneratedEntities(src);
    }
    	    	
    private void checkGeneratedEntities(String src) {
    	PackageExplorer pe = new PackageExplorer();    
    	pe.open();    	
    	try {
    		// need to wait here to get treeitem ready, TODO: implement proper condition
    		AbstractWait.sleep(TimePeriod.NORMAL);
    		new DefaultTreeItem(prj,src,"org.gen","Actor.java").doubleClick();
    	}
    	catch (SWTLayerException e) {
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
		re.save();
	}
	
	private void setHibernateSettings() {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(prj);
		
		ProjectPropertyDialog prjDlg = new ProjectPropertyDialog(prj);
		prjDlg.open();
		prjDlg.select("Hibernate Settings");
		
		CheckBox cb = new CheckBox();		
		if (!cb.isChecked()) cb.click();
		
		new DefaultCombo().setSelection(prj);
		new OkButton().click();
	}
}