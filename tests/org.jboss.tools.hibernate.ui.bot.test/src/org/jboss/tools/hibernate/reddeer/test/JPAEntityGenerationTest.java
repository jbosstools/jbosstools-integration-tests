package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.impl.tree.TreeItemNotFoundException;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
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

	private String prj = "mvn-hibernate43"; 
	private String jpaVersion = "2.0";
	 
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
    
	private void prepare() {
    	importProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDefinition(cfg);		
		ConnectionProfileFactory.createConnectionProfile(cfg);
		ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);		
	}
    
    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.jpaVersion = jpaVersion;
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
        
    private void testEntityGeneration(boolean useHibernateConsole) {
    	prepare();
    	
    	DatabaseConfiguration cfg = dbRequirement.getConfiguration();
    	EntityGenerationFactory.generateJPAEntities(cfg,prj,"org.gen","4.3",useHibernateConsole);
    	
    	ProjectExplorer pe = new ProjectExplorer();    
    	pe.open();
    	pe.selectProjects(prj);
    	try {
    		new DefaultTreeItem(prj,"src/main/java","org.gen","Actor.java").doubleClick();
    	}
    	catch (TreeItemNotFoundException e) {
    		fail("Entities not generated, possible cause https://issues.jboss.org/browse/JBIDE-19175");
    	}
    	new DefaultEditor("Actor.java");
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