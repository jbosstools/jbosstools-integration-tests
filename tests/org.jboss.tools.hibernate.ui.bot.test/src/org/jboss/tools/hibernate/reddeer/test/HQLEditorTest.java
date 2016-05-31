package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.editor.HQLEditor;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.reddeer.view.QueryPageTabView;
import org.jboss.tools.hibernate.reddeer.wizard.ConsoleConfigurationCreationWizardPage;
import org.jboss.tools.hibernate.reddeer.wizard.HibernateConsoleConnectionType;
import org.jboss.tools.hibernate.reddeer.wizard.HibernateConsoleType;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Hibernate HQL Editor test 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class HQLEditorTest extends HibernateRedDeerTest {

	private String prj = "mvn-hibernate43";
	private String hbVersion = "4.3";
	private String jpaVersion = "2.1";
	
	private static final Logger log = Logger.getLogger(HQLEditorTest.class);
	
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
    @After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
		deleteAllProjects();
	}
    
	private void prepare() {

		log.step("Import testing project");
    	importMavenProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create database connection profile");
		ConnectionProfileFactory.createConnectionProfile(cfg);
		//log.step("Convert Project to Faceted form");
		//ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		log.step("Set JPA Project facets");
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
	}

    @Test
    public void testHQLEditor35() {
    	setParams("mvn-hibernate35-ent","3.5","2.0");
    	testHQLEditor();
    }
    
    @Test
    public void testHQLEditor36() {
    	setParams("mvn-hibernate36-ent","3.6","2.0");
    	testHQLEditor();
    }
    
    @Test
    public void testHQLEditor40() {
    	setParams("mvn-hibernate40-ent","4.0","2.0");
    	testHQLEditor();
    }
    
    @Test
    public void testHQLEditor43() {
    	setParams("mvn-hibernate43-ent","4.3","2.1");
    	testHQLEditor();
    }
    
    @Test
    public void testHQLEditor50() {
    	setParams("mvn-hibernate50-ent","5.0","2.1");
    	testHQLEditor();
    }
    
    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.jpaVersion = jpaVersion;
    }
    

	public void testHQLEditor() {
		prepare();
		
		log.step("Open Hibernate Console configurations view");
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		log.step("Open and configure hibernate configuration " + prj);
		v.openConsoleConfiguration(prj);
				
		ConsoleConfigurationCreationWizardPage p = new ConsoleConfigurationCreationWizardPage();
		p.setProject(prj);
		p.setHibernateConsoleType(HibernateConsoleType.JPA);
		p.setHibernateConsoleConnectionType(HibernateConsoleConnectionType.JPA);
		p.setHibernateVersion(hbVersion);		
		log.step("Click ok to save and close the page");
		p.ok();
				
		v.open();		
		v.selectConsole(prj);
		log.step("Open HQL Editor");
		new ContextMenu("HQL Editor").select();
				
		HQLEditor hqlEditor = new HQLEditor(prj);
		log.step("Set query");
		hqlEditor.setText("from Actor");
		hqlEditor.save();
		log.step("Execute query");
		hqlEditor.runHQLQuery();
		
		QueryPageTabView result = new QueryPageTabView();
    	result.open();
    	assertTrue("Query result items expected", result.getResultItems().size() > 10);
	}

   
	
}
