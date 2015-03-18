package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.projects;

import org.jboss.ide.eclipse.as.ui.bot.test.as40.CreateAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.DeleteServerAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.OperateAS40Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateAS40Server.class,
		OperateAS40Server.class,
		DeleteServerAS40Server.class,
})
public class AS40TestsSuite {

}
