package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.tools.hibernate.reddeer.factory.JPAProjectFactory;
import org.jboss.tools.hibernate.reddeer.wizard.JpaPlatform;
import org.jboss.tools.hibernate.reddeer.wizard.JpaVersion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Create JPA Project test
 * 
 * @author Jiri Peterka
 * 
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class CreateJPAProjectTest extends HibernateRedDeerTest {	
	
	@Before 
	public void before() {
		CleanWorkspaceRequirement req = new CleanWorkspaceRequirement();
		req.fulfill();
	}

	@Test
	public void createJPAProject10() {
		JPAProjectFactory.createProject("jpa10test", JpaVersion.JPA10, JpaPlatform.HIBERNATE10);
	}

	@Test
	public void createJPAProject20() {
		JPAProjectFactory.createProject("jpa20test", JpaVersion.JPA20, JpaPlatform.HIBERNATE20);
	}

	@Test
	public void createJPAProject21() {
		JPAProjectFactory.createProject("jpa21test", JpaVersion.JPA21, JpaPlatform.HIBERNATE21);
	}
}
