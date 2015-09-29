package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.projects;

import org.jboss.ide.eclipse.as.ui.bot.test.as3.CreateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.DeleteServerAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.OperateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.CreateAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.DeleteServerAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as40.OperateAS40Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.CreateAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.DeleteServerAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.OperateAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.CreateAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.DeleteServerAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as50.OperateAS50Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.CreateAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.DeleteServerAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as51.OperateAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.CreateAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.DeleteServerAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.OperateAS6Server;
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
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.CreateWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.DeleteServerWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.DeployJSPProjectWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.HotDeployJSPFileWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.OperateWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.ServerStateDetectorsWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly10.UndeployJSPProjectWildfly10Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.CreateWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.DeleteServerWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.DeployJSPProjectWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.HotDeployJSPFileWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.OperateWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.ServerStateDetectorsWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.UndeployJSPProjectWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.CreateWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.DeleteServerWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.DeployJSPProjectWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.HotDeployJSPFileWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.OperateWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.ServerStateDetectorsWildfly9Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly9.UndeployJSPProjectWildfly9Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateWildfly10Server.class,
		CreateWildfly9Server.class,
		CreateWildfly8Server.class,
		CreateAS71Server.class,
		CreateAS70Server.class,
		CreateAS6Server.class,
		CreateAS51Server.class,
		CreateAS50Server.class,
		CreateAS42Server.class,
		CreateAS40Server.class,
		CreateAS3Server.class,
		
		OperateWildfly10Server.class,
		OperateWildfly9Server.class,
		OperateWildfly8Server.class,
		OperateAS71Server.class,
		OperateAS70Server.class,
		OperateAS6Server.class,
		OperateAS51Server.class,
		OperateAS50Server.class,
		OperateAS42Server.class,
		OperateAS40Server.class,
		OperateAS3Server.class,
		
		ServerStateDetectorsWildfly10Server.class,
		ServerStateDetectorsWildfly9Server.class,
		ServerStateDetectorsWildfly8Server.class,
		
		DeployJSPProjectWildfly10Server.class,
		DeployJSPProjectWildfly9Server.class,
		DeployJSPProjectWildfly8Server.class,
		DeployJSPProjectAS71Server.class,
		DeployJSPProjectAS70Server.class,
		
		HotDeployJSPFileWildfly10Server.class,
		HotDeployJSPFileWildfly9Server.class,
		HotDeployJSPFileWildfly8Server.class,		
		HotDeployJSPFileAS71Server.class,
		HotDeployJSPFileAS70Server.class,

		UndeployJSPProjectWildfly10Server.class,
		UndeployJSPProjectWildfly9Server.class,
		UndeployJSPProjectWildfly8Server.class,
		UndeployJSPProjectAS71Server.class,
		UndeployJSPProjectAS70Server.class,
		
		DeleteServerWildfly10Server.class,
		DeleteServerWildfly9Server.class,
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
/**
 * Test suite for JBoss AS and Wildfly server contains tests for
 * creation, start, stop, restart and deletion of the server. 
 * 
 * AS 7.0 and higher includes also tests for deploy, hot-deploy and undeploy of the 
 * application. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ProjectsTestsSuite {

}
