package org.jboss.tools.smoke.ui.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.freemarker.ui.bot.test.FreemarkerPreferencePageTest;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationFileTest;
import org.jboss.tools.hibernate.reddeer.test.CreateSingleJPAProjectTest;
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

	// Hibernate smoke tests
	EmptyTest.class,
	// temporarily disabled due to never ending build issue on Windows
	// ConsoleConfigurationFileTest.class,
	CreateSingleJPAProjectTest.class,
	HibernateUIPartsTest.class,
	JPAUIPartsTest.class,
	MappingFileTest.class,
	MavenizedProjectTest.class,
	PersistenceXMLFileTest.class,
	
	// FreeMarker smoke tests added
	FreemarkerPreferencePageTest.class,
	FreemarkerPreferencePageTest.class
	
	
	// Server smoke tests
	
	// ...
	
})
public class SmokeSuite {

}
