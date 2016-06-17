package org.jboss.tools.docker.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.docker.ui.bot.test.connection.AddConnectionTest;
import org.jboss.tools.docker.ui.bot.test.container.DockerContainerTest;
import org.jboss.tools.docker.ui.bot.test.image.BuildImageTest;
import org.jboss.tools.docker.ui.bot.test.image.PullImageTest;
import org.jboss.tools.docker.ui.bot.test.ui.PerspectiveTest;
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
	BuildImageTest.class,
	PullImageTest.class,
	DockerContainerTest.class
})

public class SmokeSuite {

}
