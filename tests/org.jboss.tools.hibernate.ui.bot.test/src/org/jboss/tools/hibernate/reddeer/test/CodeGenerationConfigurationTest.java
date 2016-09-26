package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.condition.EntityIsGenerated;
import org.jboss.tools.hibernate.reddeer.dialog.LaunchConfigurationsDialog;
import org.jboss.tools.hibernate.reddeer.editor.RevengEditor;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.wizard.NewReverseEngineeringFileWizard;
import org.jboss.tools.hibernate.reddeer.wizard.TableFilterWizardPage;
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
    
	private String prj; 
	private String hbVersion;
	private Map<String,String> libraryPathMap;
	
	private static final Logger log = Logger.getLogger(CodeGenerationConfigurationTest.class);
	
	@After
	public void cleanUp() {
		deleteAllProjects();
	}

	// Mavenized projects
    @Test
    public void testHibernateGenerateConfiguration35() {
    	setParams("mvn-hibernate35","3.5","2.0", null);
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration36() {
    	setParams("mvn-hibernate36","3.6","2.0", null);
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration40() {
    	setParams("mvn-hibernate40","4.0","2.0", null);
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration43() {
    	setParams("mvn-hibernate43","4.3","2.1", null);
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration50() {
    	setParams("mvn-hibernate50","5.0","2.1", null);
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration51() {
    	setParams("mvn-hibernate51","5.1","2.1", null);
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfiguration52() {
    	setParams("mvn-hibernate52","5.2","2.1", null);
    	createHibernateGenerationConfigurationMvn(false);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng35() {
    	setParams("mvn-hibernate35","3.5","2.0", null);
    	createHibernateGenerationConfigurationMvn(true);
    }
        
    @Test
    public void testHibernateGenerateConfigurationWithReveng36() {
    	setParams("mvn-hibernate36","3.6","2.0", null);
     	createHibernateGenerationConfigurationMvn(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng40() {
    	setParams("mvn-hibernate40","4.0","2.0", null);
    	createHibernateGenerationConfigurationMvn(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng43() {
    	setParams("mvn-hibernate43","4.3","2.1", null);
    	createHibernateGenerationConfigurationMvn(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng50() {
    	setParams("mvn-hibernate50","5.0","2.1", null);
    	createHibernateGenerationConfigurationMvn(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng51() {
    	setParams("mvn-hibernate51","5.1","2.1", null);
    	createHibernateGenerationConfigurationMvn(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationWithReveng52() {
    	setParams("mvn-hibernate52","5.2","2.1", null);
    	createHibernateGenerationConfigurationMvn(true);
    }
    
    // Non-mavenized projects
    @Test
    public void testHibernateGenerateConfigurationEcl35() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate35LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate35-ent","3.5","2.0", libraries);
    	createHibernateGenerationConfigurationEcl(false);
    }
 
    @Test
    public void testHibernateGenerateConfigurationEcl36() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate36LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate36-ent","3.6","2.0", libraries);
    	createHibernateGenerationConfigurationEcl(false);
    }
    
    @Test
    public void testHibernateGenerateConfigurationEcl40() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate43LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate40-ent","4.3","2.0", libraries);
    	createHibernateGenerationConfigurationEcl(false);
    }
    @Test
    public void testHibernateGenerateConfigurationRelEcl35() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate35LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate35-ent","3.5","2.0", libraries);
    	createHibernateGenerationConfigurationEcl(true);
    }
 
    @Test
    public void testHibernateGenerateConfigurationRelEcl36() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate36LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate36-ent","3.6","2.0", libraries);
    	createHibernateGenerationConfigurationEcl(true);
    }
    
    @Test
    public void testHibernateGenerateConfigurationRelEcl40() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate43LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate40-ent","4.3","2.0", libraries);
    	createHibernateGenerationConfigurationEcl(true);
    }

    
	private void prepareMvn() {
		
		log.step("Import maven project");
    	importMavenProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Create Hibernate configuration file with Hibernate Console");
		HibernateToolsFactory.createConfigurationFile(cfg, prj, "hibernate.cfg.xml", true);
		log.step("Set hibernate version in Hibernate Console");
		HibernateToolsFactory.setHibernateVersion(prj, hbVersion);
	}

	private void prepareEcl() {
		
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Import hibernatelib and java project");	
    	importProject(prj, libraryPathMap);
    	
    	log.step("Create Hibernate configuration file with Hibernate Console");
		HibernateToolsFactory.createConfigurationFile(cfg, prj, "hibernate.cfg.xml", true);
		log.step("Set Hibernate Version in Hibernate Console");
		HibernateToolsFactory.setHibernateVersion(prj, hbVersion);
		log.step("Hibernate console in Hibernate Settings in Project Properties");
		setHibernateSettings(prj);
	}

	
    private void setParams(String prj, String hbVersion, String jpaVersion, Map<String,String> libraryPathMap) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.libraryPathMap = libraryPathMap;
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
    		//AbstractWait.sleep(TimePeriod.NORMAL);
    		new WaitUntil(new EntityIsGenerated(prj, src, "org.gen", "Actor.java"));
    		//new WaitUntil(new ProjectContainsProjectItem(pe.getProject(prj), src,"org.gen","Actor.java"));
    		pe.getProject(prj).getProjectItem(src,"org.gen","Actor.java").open();
    	}
    	catch (RedDeerException e) {
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
		pe.getProject(prj).getProjectItem("hibernate.reveng.xml").open();
		new DefaultEditor("Hibernate Reverse Engineering Editor").activate();
		
		RevengEditor re = new RevengEditor();
		re.activateDesignTab();
		re.activateOverviewTab();
		re.activateTableFiltersTab();
		re.activateTypeMappingsTab();
		re.activateTableAndColumnsTab();
		try {
			re.selectAllTables("SAKILA.PUBLIC");
		} catch (WaitTimeoutExpiredException e) {
			fail("Cannot add tables - known issue(s) - JBIDE-19443");
		}
		re.activateSourceTab();
		re.save();
	}
}