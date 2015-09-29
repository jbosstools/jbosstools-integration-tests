package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.projects;

import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.CreateWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.DeleteServerWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.DeployJSPProjectWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.HotDeployJSPFileWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.OperateWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.UndeployJSPProjectWildfly10Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateWildfly10Server.class,
		OperateWildfly10Server.class,
		DeployJSPProjectWildfly10Server.class,
		HotDeployJSPFileWildfly10Server.class,
		UndeployJSPProjectWildfly10Server.class,
		DeleteServerWildfly10Server.class
})
public class Wildfly10TestsSuite {

}
