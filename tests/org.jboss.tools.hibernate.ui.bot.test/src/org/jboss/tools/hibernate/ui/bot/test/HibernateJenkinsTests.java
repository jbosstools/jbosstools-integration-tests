package org.jboss.tools.hibernate.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.hibernate.reddeer.test.AntFileExportTest;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationFileTest;
import org.jboss.tools.hibernate.reddeer.test.HibernateUIPartsTest;
import org.jboss.tools.hibernate.reddeer.test.RevengFileTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Initial Hibernate ui tests for jenkins suite 
 * @author jpeterka
 *
 */
@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({

	HibernateUIPartsTest.class,
	AntFileExportTest.class,
	RevengFileTest.class,
	ConsoleConfigurationFileTest.class
})
public class HibernateJenkinsTests {

}