package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.projects;

import org.jboss.ide.eclipse.as.ui.bot.test.as3.CreateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.DeleteServerAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.OperateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.CreateAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.DeleteServerAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.DeployJSPProjectAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.HotDeployJSPFileAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.OperateAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.UndeployJSPProjectAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.CreateAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.DeleteServerAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.DeployJSPProjectAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.HotDeployJSPFileAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.OperateAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.UndeployJSPProjectAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.CreateAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.DeleteServerAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.DeployJSPProjectAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.HotDeployJSPFileAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.OperateAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.UndeployJSPProjectAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.CreateAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.DeleteServerAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.DeployJSPProjectAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.HotDeployJSPFileAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.OperateAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.UndeployJSPProjectAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.CreateAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.DeleteServerAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.DeployJSPProjectAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.HotDeployJSPFileAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.OperateAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.UndeployJSPProjectAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.CreateAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.DeleteServerAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.DeployJSPProjectAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.HotDeployJSPFileAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.OperateAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.UndeployJSPProjectAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.CreateAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.DeleteServerAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.DeployJSPProjectAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.HotDeployJSPFileAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.OperateAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.UndeployJSPProjectAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.CreateWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.DeleteServerWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.DeployJSPProjectWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.HotDeployJSPFileWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.OperateWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.ServerStateDetectorsWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.UndeployJSPProjectWildfly8Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateWildfly8Server.class,
		CreateAS71Server.class,
		CreateAS70Server.class,
		CreateAS6Server.class,
		CreateAS51Server.class,
		CreateAS50Server.class,
		CreateAS42Server.class,
		CreateAS40Server.class,
		CreateAS3Server.class,
		
		OperateWildfly8Server.class,
		OperateAS71Server.class,
		OperateAS70Server.class,
		OperateAS6Server.class,
		OperateAS51Server.class,
		OperateAS50Server.class,
		OperateAS42Server.class,
		OperateAS40Server.class,
		OperateAS3Server.class,
		
		ServerStateDetectorsWildfly8Server.class,
		
		DeployJSPProjectWildfly8Server.class,
		DeployJSPProjectAS71Server.class,
		DeployJSPProjectAS70Server.class,
		DeployJSPProjectAS6Server.class,
		DeployJSPProjectAS51Server.class,
		DeployJSPProjectAS50Server.class,
		DeployJSPProjectAS42Server.class,
		DeployJSPProjectAS40Server.class,
		// do not test deploy for AS 3 - failed compile
		// DeployJSPProjectAS3Server.class,
		
		HotDeployJSPFileWildfly8Server.class,		
		HotDeployJSPFileAS71Server.class,
		HotDeployJSPFileAS70Server.class,
		HotDeployJSPFileAS6Server.class,
		HotDeployJSPFileAS51Server.class,
		HotDeployJSPFileAS50Server.class,
		HotDeployJSPFileAS42Server.class,
		HotDeployJSPFileAS40Server.class,
		// do not test deploy for AS 3 - failed compile
		// HotDeployJSPFileAS3Server.class,

		UndeployJSPProjectWildfly8Server.class,
		UndeployJSPProjectAS71Server.class,
		UndeployJSPProjectAS70Server.class,
		UndeployJSPProjectAS6Server.class,
		UndeployJSPProjectAS51Server.class,
		UndeployJSPProjectAS50Server.class,
		UndeployJSPProjectAS42Server.class,
		UndeployJSPProjectAS40Server.class,
		// do not test deploy for AS 3 - failed compile
		// UndeployJSPProjectAS3Server.class,
		
		DeleteServerWildfly8Server.class,
		DeleteServerAS71Server.class,
		DeleteServerAS70Server.class,
		DeleteServerAS6Server.class,
		DeleteServerAS51Server.class,
		DeleteServerAS50Server.class,
		DeleteServerAS42Server.class,
		DeleteServerAS40Server.class,
		DeleteServerAS3Server.class
})
public class ProjectsTestsSuite {

}
