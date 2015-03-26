package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.impl.tree.TreeItemNotFoundException;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.EntityGenerationFactory;
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

	private String prj = "mvn-hibernate43";
	private String hbVersion = "4.3";
	private String jpaVersion = "2.0";

	private Logger log = Logger.getLogger(this.getClass());
	
	private final String DDL_FILE = "output.ddl";
	@InjectRequirement
	private DatabaseRequirement dbRequirement;
	
	public void prepare() {
		log.step("Import test project");
		importProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create connection profile definition");
		ConnectionProfileFactory.createConnectionProfile(cfg);
		log.step("Convert project to faceted from");
		ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		log.step("Set JPA facets to Hibernate Platform");
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
		log.step("Generate JPA Entities");
		EntityGenerationFactory.generateJPAEntities(cfg, prj, "org.gen", hbVersion, true);
	}
	
    // don't use console
    @Test
    public void testDDLGenerationWithConsole35() {
    	setParams("mvn-hibernate35","3.5","2.0");
    	testDDLGeneration(false);
    }
    
    @Test
    public void testDDLGenerationWithConsole36() {
    	setParams("mvn-hibernate36","3.6","2.0");
    	testDDLGeneration(false);
    }
    
    @Test
    public void testDDLGenerationWithConsole40() {
    	setParams("mvn-hibernate40","4.0","2.0");
    	testDDLGeneration(false);
    }
    
    @Test
    public void testDDLGenerationWithConsole43() {
    	setParams("mvn-hibernate43","4.3","2.1");
    	testDDLGeneration(false);
    }
    
    // use console
    @Test
    public void testDDLGenerationWithoutConsole35() {
    	setParams("mvn-hibernate35","3.5","2.0");
    	testDDLGeneration(true);
    }
    
    @Test
    public void testDDLGenerationWithoutConsole36() {
    	setParams("mvn-hibernate36","3.6","2.0");
    	testDDLGeneration(true);
    }
    
    @Test
    public void testDDLGenerationWithoutConsole40() {
    	setParams("mvn-hibernate40","4.0","2.0");
    	testDDLGeneration(true);
    }
    
    @Test
    public void testDDLGenerationWithoutConsole43() {
    	setParams("mvn-hibernate43","4.3","2.1");
    	testDDLGeneration(true);
    }
    

    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.jpaVersion = jpaVersion;
    }
    
	private void testDDLGeneration(boolean useConsole) {
		prepare();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(prj);
		GenerateDdlWizard w = new GenerateDdlWizard();
		w.open();
		log.step("Open Generate Tables from Entitities wizard");
		GenerateDdlWizardPage p = new GenerateDdlWizardPage();
		p.setFileName(DDL_FILE);
		p.setUseConsoleConfiguration(useConsole);
		log.step("Click finish to generate ddl");
		w.finish();

		pe.open();
		try {
			new DefaultTreeItem(prj,"src/main/java", DDL_FILE).doubleClick();
		} catch (TreeItemNotFoundException e) {
			Assert.fail("DDL is not generated - known issues(s): JBIDE-19431");	
		}
		String ddlText = new TextEditor(DDL_FILE).getText();
		assertTrue("DDL file cannot be empty", ddlText.length() > 0);
		checkDDLContent(ddlText);
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
	
	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(prj).delete(true);
	}
}