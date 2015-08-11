package org.jboss.tools.smoke.ui.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationFileTest;
import org.jboss.tools.hibernate.reddeer.test.CreateJPAProjectTest;
import org.jboss.tools.hibernate.reddeer.test.EmptyTest;
import org.jboss.tools.hibernate.reddeer.test.HibernateUIPartsTest;
import org.jboss.tools.hibernate.reddeer.test.JPAUIPartsTest;
import org.jboss.tools.hibernate.reddeer.test.MappingFileTest;
import org.jboss.tools.hibernate.reddeer.test.MavenizedProjectTest;
import org.jboss.tools.hibernate.reddeer.test.PersistenceXMLFileTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({

	// Hibernate Smoke Tests
	EmptyTest.class,
	ConsoleConfigurationFileTest.class,
	CreateJPAProjectTest.class,
	HibernateUIPartsTest.class,
	JPAUIPartsTest.class,
	MappingFileTest.class,
	MavenizedProjectTest.class,
	PersistenceXMLFileTest.class
	
	// Server Smoke Tests
	
	// ...
	
})
public class SmokeSuite {

}