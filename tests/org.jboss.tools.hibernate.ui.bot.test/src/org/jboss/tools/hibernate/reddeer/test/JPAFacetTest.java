package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.tools.hibernate.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.factory.ProjectConfigurationFactory;
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
    
    @Before
	public void testConnectionProfile() {
    	importProject(PRJ);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDefinition(cfg);
		ConnectionProfileFactory.createConnectionProfile(cfg);		
	}
    
    @Test
    public void testSetJPAFacets()
    {    	    
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