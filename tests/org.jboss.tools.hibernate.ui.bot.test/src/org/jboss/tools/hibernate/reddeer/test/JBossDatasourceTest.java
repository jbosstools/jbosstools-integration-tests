package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.tools.hibernate.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.factory.DriverDefinitionFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Tests JBoss Datasource   
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class JBossDatasourceTest {

    @InjectRequirement
    DatabaseRequirement dbRequirement;
    
	@Test
	public void testJBossDatasource() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDefinition(cfg);
		ConnectionProfileFactory.createConnectionProfile(cfg);
	}
	
	
	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
	}

}
