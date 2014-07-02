package org.jboss.tools.hibernate.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationFileTest;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationTest;
import org.jboss.tools.hibernate.reddeer.test.EmptyTest;
import org.jboss.tools.hibernate.reddeer.test.HibernateUIPartsTest;
import org.jboss.tools.hibernate.reddeer.test.JBossDatasourceTest;
import org.jboss.tools.hibernate.reddeer.test.MavenizedProjectTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({

	EmptyTest.class,
	HibernateUIPartsTest.class /*
	MavenizedProjectTest.class,	
	JBossDatasourceTest.class,
	ConsoleConfigurationFileTest.class,
	ConsoleConfigurationTest.class,
	*/
	
})
public class HibernateAllTest {

}