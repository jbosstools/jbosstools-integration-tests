package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.projects;

import org.jboss.ide.eclipse.as.ui.bot.test.as51.CreateAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.DeleteServerAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.OperateAS51Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateAS51Server.class,
		OperateAS51Server.class,
		DeleteServerAS51Server.class,
})
public class AS51TestsSuite {

}
