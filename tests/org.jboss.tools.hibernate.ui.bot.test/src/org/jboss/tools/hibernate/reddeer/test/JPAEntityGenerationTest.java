package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.condition.EntityIsGenerated;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.EntityGenerationFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test prepares project and generate JPA entities from database 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class JPAEntityGenerationTest extends HibernateRedDeerTest {

	private String prj; 
	private String jpaVersion;
	private String hbVersion;
	
	private static final Logger log = Logger.getLogger(JPAEntityGenerationTest.class);
	 
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
		log.step("Create driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create connection profile");
		ConnectionProfileFactory.createConnectionProfile(cfg);
		//log.step("Convert project to faceted form");
		//ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		log.step("Set Hibernate JPA facet");
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);		
	}
    
    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.jpaVersion = jpaVersion;
    	this.hbVersion = hbVersion;
    }
    
    @Test
    public void testEntityGenerationWithConsole35() {
    	setParams("mvn-hibernate35","3.5","2.0");
    	testEntityGeneration(true);
    }
    
    @Test
    public void testEntityGenerationWithConsole36() {
    	setParams("mvn-hibernate36","3.6","2.0");
    	testEntityGeneration(true);
    }
    
    @Test
    public void testEntityGenerationWithConsole40() {
    	setParams("mvn-hibernate40","4.0","2.0");
    	testEntityGeneration(true);
    }
    
    @Test
    public void testEntityGenerationWithConsole43() {
    	setParams("mvn-hibernate43","4.3","2.1");
    	testEntityGeneration(true);
    }
    
    @Test
    public void testEntityGenerationWithConsole50() {
    	setParams("mvn-hibernate50","5.0","2.1");
    	testEntityGeneration(true);
    }
    
    @Test
    public void testEntityGenerationWithConsole51() {
    	setParams("mvn-hibernate51","5.1","2.1");
    	testEntityGeneration(true);
    }
    
    @Test
    public void testEntityGenerationWithConsole52() {
    	setParams("mvn-hibernate52","5.2","2.1");
    	testEntityGeneration(true);
    }
    
    @Test
    public void testEntityGenerationWithoutConsole35() {
    	setParams("mvn-hibernate35","3.5","2.0");
    	testEntityGeneration(false);
    }
    
    @Test
    public void testEntityGenerationWithoutConsole36() {
    	setParams("mvn-hibernate36","3.6","2.0");
    	testEntityGeneration(false);
    }
    
    @Test
    public void testEntityGenerationWithoutConsole40() {
    	setParams("mvn-hibernate40","4.0","2.0");
    	testEntityGeneration(false);
    }
    
    @Test
    public void testEntityGenerationWithoutConsole43() {
    	setParams("mvn-hibernate43","4.3","2.1");
    	testEntityGeneration(false);
    }   
    
    @Test
    public void testEntityGenerationWithoutConsole50() {
    	setParams("mvn-hibernate50","5.0","2.1");
    	testEntityGeneration(false);
    } 
    
    @Test
    public void testEntityGenerationWithoutConsole51() {
    	setParams("mvn-hibernate51","5.1","2.1");
    	testEntityGeneration(false);
    } 
    
    @Test
    public void testEntityGenerationWithoutConsole52() {
    	setParams("mvn-hibernate52","5.2","2.1");
    	testEntityGeneration(false);
    } 
        
    private void testEntityGeneration(boolean useHibernateConsole) {
    	prepare();
    	
    	DatabaseConfiguration cfg = dbRequirement.getConfiguration();
    	log.step("Generate JPA Entities via JPA -> Generate Entities from Tables");
    	EntityGenerationFactory.generateJPAEntities(cfg,prj,"org.gen",hbVersion,useHibernateConsole);
    	
    	log.step("Check generated entities");
    	ProjectExplorer pe = new ProjectExplorer();    
    	pe.open();
    	try{
    		new WaitUntil(new EntityIsGenerated(prj, "src/main/java","org.gen","Actor.java"));
    		//AbstractWait.sleep(TimePeriod.NORMAL); //class generation is happening
    		//new WaitUntil(new ProjectContainsProjectItem(pe.getProject(prj), "Java Resources","src/main/java","org.gen","Actor.java"));
    		pe.getProject(prj).getProjectItem("Java Resources","src/main/java","org.gen","Actor.java").open();
    	} catch (RedDeerException e) {
    		e.printStackTrace();
    		fail("Entities not generated, possible cause https://issues.jboss.org/browse/JBIDE-19175");
    	}
    	new DefaultEditor("Actor.java");
    }
    
	
}