package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.reddeer.wizard.GenerateDdlWizard;
import org.jboss.tools.hibernate.reddeer.wizard.GenerateDdlWizardPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Generates ddl and tables from Entities
 * 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name = "testdb")
public class TablesFromJPAEntitiesGeneration extends HibernateRedDeerTest {

	private String prj;
	private String hbVersion;
	private String jpaVersion;
	private Map<String,String> libraryPathMap;

	private static final Logger log = Logger.getLogger(TablesFromJPAEntitiesGeneration.class);
	
	private final String DDL_FILE = "output.ddl";
	@InjectRequirement
	private DatabaseRequirement dbRequirement;
	
	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
		deleteAllProjects();
	}
	
    // use console
    @Test
    public void testDDLGenerationWithConsole35() {
    	setParams("mvn-hibernate35-ent","3.5","2.0",null);
    	testDDLGenerationMvn(true);
    }
    
    @Test
    public void testDDLGenerationWithConsole36() {
    	setParams("mvn-hibernate36-ent","3.6","2.0",null);
    	testDDLGenerationMvn(true);
    }
    
    @Test
    public void testDDLGenerationWithConsole40() {
    	setParams("mvn-hibernate40-ent","4.0","2.0",null);
    	testDDLGenerationMvn(true);
    }
    
    @Test
    public void testDDLGenerationWithConsole43() {
    	setParams("mvn-hibernate43-ent","4.3","2.1",null);
    	testDDLGenerationMvn(true);
    }
    
    @Test
    public void testDDLGenerationWithConsole50() {
    	setParams("mvn-hibernate50-ent","5.0","2.1",null);
    	testDDLGenerationMvn(true);
    }
    
    @Test
    public void testDDLGenerationWithConsole51() {
    	setParams("mvn-hibernate51-ent","5.1","2.1",null);
    	testDDLGenerationMvn(true);
    }
    
    @Test
    public void testDDLGenerationWithConsole52() {
    	setParams("mvn-hibernate52-ent","5.2","2.1",null);
    	testDDLGenerationMvn(true);
    }
   
    @Test
    public void testDDLGenerationWithConsoleEcl35() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate35LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate35-ent","3.5","2.0",libraries);
    	testDDLGenerationEcl(true);
    }
    
    @Test
    public void testDDLGenerationWithConsoleEcl36() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate36LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate36-ent","3.6","2.0", libraries);
    	testDDLGenerationEcl(true);
    }
    
    @Test
    public void testDDLGenerationWithConsoleEcl40() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate43LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate40-ent","4.3","2.0", libraries);
    	testDDLGenerationEcl(true);
    }
    
    // don't use console
    @Test
    public void testDDLGenerationWithoutConsole35() {
    	setParams("mvn-hibernate35-ent","3.5","2.0",null);
    	testDDLGenerationMvn(false);
    }
    
    @Test
    public void testDDLGenerationWithoutConsole36() {
    	setParams("mvn-hibernate36-ent","3.6","2.0",null);
    	testDDLGenerationMvn(false);
    }
    
    @Test
    public void testDDLGenerationWithoutConsole40() {
    	setParams("mvn-hibernate40-ent","4.0","2.0",null);
    	testDDLGenerationMvn(false);
    }
    
    @Test
    public void testDDLGenerationWithoutConsole43() {
    	setParams("mvn-hibernate43-ent","4.3","2.1",null);
    	testDDLGenerationMvn(false);
    }
    
    @Test
    public void testDDLGenerationWithoutConsole50() {
    	setParams("mvn-hibernate50-ent","5.0","2.1",null);
    	testDDLGenerationMvn(false);
    }
    
    @Test
    public void testDDLGenerationWithoutConsole51() {
    	setParams("mvn-hibernate51-ent","5.1","2.1",null);
    	testDDLGenerationMvn(false);
    }
    
    @Test
    public void testDDLGenerationWithoutConsole52() {
    	setParams("mvn-hibernate52-ent","5.2","2.1",null);
    	testDDLGenerationMvn(false);
    }
      
    @Test
    public void testDDLGenerationWithoutConsoleEcl35() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate35LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate35-ent","3.5","2.0",libraries);
    	testDDLGenerationEcl(false);
    }
    
    @Test
    public void testDDLGenerationWithoutConsoleEcl36() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate36LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate36-ent","3.6","2.0", libraries);
    	testDDLGenerationEcl(false);
    }

    @Test
    public void testDDLGenerationWithoutConsoleEcl40() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate43LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate40-ent","4.3","2.0", libraries);
    	testDDLGenerationEcl(false);
    }
    

    private void setParams(String prj, String hbVersion, String jpaVersion, Map<String,String> libraryPathMap) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.jpaVersion = jpaVersion;
    	this.libraryPathMap = libraryPathMap;
    }
    
    private void testDDLGenerationEcl(boolean useConsole) {
    	prepareEclipseProject();
    	testDDLGeneration(useConsole,hbVersion,"src");
    }    
    
    private void testDDLGenerationMvn(boolean useConsole) {
    	prepareMavenProject();
    	testDDLGeneration(useConsole,hbVersion, "Java Resources","src/main/java");
    }
    
	private void testDDLGeneration(boolean useConsole, String hbVersion, String... pkg ) {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(prj);
		GenerateDdlWizard w = new GenerateDdlWizard();
		w.open();
		log.step("Open Generate Tables from Entitities wizard");
		GenerateDdlWizardPage p = new GenerateDdlWizardPage();
		p.setFileName(DDL_FILE);
		p.setUseConsoleConfiguration(useConsole);
		if (!useConsole) {
			p.setHibernateVersion(hbVersion);
		}
		log.step("Click finish to generate ddl");
		w.finish();

		pe.open();
		try {
			pe.getProject(prj).getProjectItem(pkg).getProjectItem(DDL_FILE).open();  
		} catch (RedDeerException e) {
			Assert.fail("DDL is not generated - known issues(s): JBIDE-19431,JBIDE-19535");	
		}
		String ddlText = new TextEditor(DDL_FILE).getText();
		assertTrue("DDL file cannot be empty", ddlText.length() > 0);
		checkDDLContent(ddlText);
	}

	private void prepareMavenProject() {
		log.step("Import test project");
		importMavenProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create connection profile definition");
		ConnectionProfileFactory.createConnectionProfile(cfg);
		//log.step("Convert project to faceted from");
		//ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		log.step("Set JPA facets to Hibernate Platform");
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
	}
    
    private void prepareEclipseProject() {    	
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Import test projects");
    	importProject(prj, libraryPathMap);

		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create connection profile definition");
		ConnectionProfileFactory.createConnectionProfile(cfg);
    	
		//log.step("Convert project to faceted from");
		//ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		log.step("Set JPA facets to Hibernate Platform");
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
    	
    	log.step("Create hibernate console configuartion file");
    	HibernateToolsFactory.createConfigurationFile(cfg, prj, "hibernate.cfg.xml", false);
	}
	
	private void checkDDLContent(String text) {
		
		String[] expected = { 
		"create table SAKILA.PUBLIC.ADDRESS",
		"create table SAKILA.PUBLIC.CATEGORY",
		"create table SAKILA.PUBLIC.CITY",
		"create table SAKILA.PUBLIC.COUNTRY",
		"create table SAKILA.PUBLIC.CUSTOMER",
		"create table SAKILA.PUBLIC.FILM",
		"create table SAKILA.PUBLIC.FILM_ACTOR",
		"create table SAKILA.PUBLIC.FILM_CATEGORY",
		"create table SAKILA.PUBLIC.FILM_TEXT",
		"create table SAKILA.PUBLIC.INVENTORY",
		"create table SAKILA.PUBLIC.LANGUAGE",
		"create table SAKILA.PUBLIC.PAYMENT",
		"create table SAKILA.PUBLIC.RENTAL",
		"create table SAKILA.PUBLIC.STAFF",
		"create table SAKILA.PUBLIC.STORE",
		"alter table SAKILA.PUBLIC.ADDRESS",
		"alter table SAKILA.PUBLIC.CITY",
		"alter table SAKILA.PUBLIC.CUSTOMER",
		"alter table SAKILA.PUBLIC.FILM",
		"alter table SAKILA.PUBLIC.FILM_ACTOR",
		"alter table SAKILA.PUBLIC.FILM_CATEGORY",
		"alter table SAKILA.PUBLIC.INVENTORY",
		"alter table SAKILA.PUBLIC.INVENTORY",
		"alter table SAKILA.PUBLIC.PAYMENT",
		"alter table SAKILA.PUBLIC.RENTAL",
		"alter table SAKILA.PUBLIC.STAFF",
		"alter table SAKILA.PUBLIC.STORE"};
		
		for (String s : expected) {
			assertTrue(s + " is expected in ddl", text.contains(s));
		}
	}
}