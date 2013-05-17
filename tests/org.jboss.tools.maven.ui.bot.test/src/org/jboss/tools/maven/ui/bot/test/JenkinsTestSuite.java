package org.jboss.tools.maven.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
	
	ArchetypesTest.class,
	PerspectiveTest.class,
	MavenProfilesTest.class,
	MaterializeLibraryTest.class,
	//EARProjectTest.class,
	SeamProjectTest.class, 
	//JSFProjectTest.class, 
	JPAConfiguratorTest.class,
	JSFConfiguratorTest.class,
	SeamConfiguratorTest.class,
	CDIConfiguratorTest.class, 
	JAXRSConfiguratorTest.class,
	MavenRepositories.class
})
public class JenkinsTestSuite {

}
