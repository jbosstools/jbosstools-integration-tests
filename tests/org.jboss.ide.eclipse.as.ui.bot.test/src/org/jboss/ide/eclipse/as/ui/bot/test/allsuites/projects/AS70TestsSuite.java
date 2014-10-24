package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.projects;

import org.jboss.ide.eclipse.as.ui.bot.test.as70.CreateAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.DeleteServerAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.DeployJSPProjectAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.HotDeployJSPFileAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.OperateAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.UndeployJSPProjectAS70Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateAS70Server.class,
		OperateAS70Server.class,
		DeployJSPProjectAS70Server.class,
		HotDeployJSPFileAS70Server.class,
		UndeployJSPProjectAS70Server.class,
		DeleteServerAS70Server.class,
})
public class AS70TestsSuite {

}
