package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Set JPA Facets for JPA project 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class JPAFacetTest extends HibernateRedDeerTest {

	private final String PRJ = "mvn-hibernate43"; 
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
    private Logger log = Logger.getLogger(this.getClass());
    
    @Before
	public void testConnectionProfile() {
    	log.step("Import test project");
    	importProject(PRJ);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Convert database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Convert database driver definition");
		ConnectionProfileFactory.createConnectionProfile(cfg);		
	}
    
    @Test
    public void testSetJPAFacets()
    {    	    
    	log.step("Set JPA facet for Hibernate");
    	DatabaseConfiguration cfg = dbRequirement.getConfiguration();
    	ProjectConfigurationFactory.convertProjectToFacetsForm(PRJ);
    	ProjectConfigurationFactory.setProjectFacetForDB(PRJ, cfg);
    }
    
	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
	}
}