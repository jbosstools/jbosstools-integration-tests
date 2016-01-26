package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.jboss.tools.hibernate.reddeer.factory.JPAProjectFactory;
import org.jboss.tools.hibernate.reddeer.wizard.JpaPlatform;
import org.jboss.tools.hibernate.reddeer.wizard.JpaVersion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Create Single JPA Project test
 * 
 * @author Jiri Peterka
 * 
 */
@RunWith(RedDeerSuite.class)
public class CreateSingleJPAProjectTest extends HibernateRedDeerTest {

	final String PROJECT_NAME = "jpa35test";	
	
	@Before 
	public void before() {
		CleanWorkspaceRequirement req = new CleanWorkspaceRequirement();
		req.fulfill();
	}


	@Test
	public void createJPAProject21() {
		JPAProjectFactory.createProject("jpa21test", JpaVersion.JPA21, JpaPlatform.HIBERNATE21);
	}
}
