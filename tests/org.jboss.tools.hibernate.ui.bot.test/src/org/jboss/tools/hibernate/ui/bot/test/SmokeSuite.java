package org.jboss.tools.hibernate.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.hibernate.reddeer.test.CodeGenerationConfigurationTest;
import org.jboss.tools.hibernate.reddeer.test.ConnectionProfileTest;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationFileTest;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationTest;
import org.jboss.tools.hibernate.reddeer.test.CreateJPAProjectTest;
import org.jboss.tools.hibernate.reddeer.test.CriteriaEditorTest;
import org.jboss.tools.hibernate.reddeer.test.HQLEditorTest;
import org.jboss.tools.hibernate.reddeer.test.HibernateUIPartsTest;
import org.jboss.tools.hibernate.reddeer.test.JPAEntityGenerationTest;
import org.jboss.tools.hibernate.reddeer.test.JPAFacetTest;
import org.jboss.tools.hibernate.reddeer.test.JPAUIPartsTest;
import org.jboss.tools.hibernate.reddeer.test.JpaAnnotationGenerationTest;
import org.jboss.tools.hibernate.reddeer.test.MavenizedProjectTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({

	CodeGenerationConfigurationTest.class,
	ConnectionProfileTest.class,
	ConsoleConfigurationFileTest.class,
	ConsoleConfigurationTest.class,
	CreateJPAProjectTest.class,
	CriteriaEditorTest.class,
	HibernateUIPartsTest.class,
	HQLEditorTest.class,
	JpaAnnotationGenerationTest.class,
	JPAEntityGenerationTest.class,
	JPAFacetTest.class,
	MavenizedProjectTest.class,
	JPAUIPartsTest.class
})
public class SmokeSuite {

}
