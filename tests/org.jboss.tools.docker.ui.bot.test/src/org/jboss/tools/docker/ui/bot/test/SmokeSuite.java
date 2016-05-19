package org.jboss.tools.docker.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.docker.ui.bot.test.connection.AddConnectionTest;
import org.jboss.tools.docker.ui.bot.test.ui.DockerContainerTest;
import org.jboss.tools.docker.ui.bot.test.ui.PerspectiveTest;
import org.jboss.tools.docker.ui.bot.test.ui.PullImageTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 
 * SmokeSuite for Docker tests.
 * 
 * @author jkopriva
 */

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
	PerspectiveTest.class, 
	AddConnectionTest.class,
	PullImageTest.class,
	DockerContainerTest.class,
})

public class SmokeSuite {

}
