package org.jboss.tools.maven.ui.bot.test;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.maven.ui.bot.test.conversion.MavenConversionTest;
import org.jboss.tools.maven.ui.bot.test.profile.MavenProfilesTest;
import org.jboss.tools.maven.ui.bot.test.project.ArchetypesTest;
import org.jboss.tools.maven.ui.bot.test.repository.MavenRepositories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
	ArchetypesTest.class,
	MavenRepositories.class,
	MavenConversionTest.class,
	MavenProfilesTest.class
})
public class SmokeSuite {

}
