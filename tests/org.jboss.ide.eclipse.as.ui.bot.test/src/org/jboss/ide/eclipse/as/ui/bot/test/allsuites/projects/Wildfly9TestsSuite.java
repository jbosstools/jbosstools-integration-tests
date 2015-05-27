package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.projects;

import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.CreateWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.DeleteServerWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.DeployJSPProjectWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.HotDeployJSPFileWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.OperateWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.UndeployJSPProjectWildfly9Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateWildfly9Server.class,
		OperateWildfly9Server.class,
		DeployJSPProjectWildfly9Server.class,
		HotDeployJSPFileWildfly9Server.class,
		UndeployJSPProjectWildfly9Server.class,
		DeleteServerWildfly9Server.class
})
public class Wildfly9TestsSuite {

}
