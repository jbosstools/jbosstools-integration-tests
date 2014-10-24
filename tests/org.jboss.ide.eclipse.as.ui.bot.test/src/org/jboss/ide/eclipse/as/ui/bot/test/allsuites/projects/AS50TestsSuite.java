package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.projects;

import org.jboss.ide.eclipse.as.ui.bot.test.as50.CreateAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.DeleteServerAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.DeployJSPProjectAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.HotDeployJSPFileAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.OperateAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.UndeployJSPProjectAS50Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateAS50Server.class,
		OperateAS50Server.class,
		DeployJSPProjectAS50Server.class,
		HotDeployJSPFileAS50Server.class,
		UndeployJSPProjectAS50Server.class,
		DeleteServerAS50Server.class,
})
public class AS50TestsSuite {

}
