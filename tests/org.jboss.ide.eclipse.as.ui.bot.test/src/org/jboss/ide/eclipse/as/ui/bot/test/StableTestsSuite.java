package org.jboss.ide.eclipse.as.ui.bot.test;

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
import org.jboss.ide.eclipse.as.ui.bot.test.as71.ServerStateDetectorsAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.UndeployJSPProjectAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.CreateWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.DeleteServerWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.DeployJSPProjectWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.HotDeployJSPFileWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.OperateWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.ServerStateDetectorsWildfly8Server;
import org.jboss.ide.eclipse.as.ui.bot.test.wildfly8.UndeployJSPProjectWildfly8Server;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateWildfly8Server.class,
		OperateWildfly8Server.class,
		ServerStateDetectorsWildfly8Server.class,
		DeployJSPProjectWildfly8Server.class,
		HotDeployJSPFileWildfly8Server.class,
		UndeployJSPProjectWildfly8Server.class,
		DeleteServerWildfly8Server.class,
	
		CreateAS71Server.class,
		OperateAS71Server.class,
		ServerStateDetectorsAS71Server.class,
		DeployJSPProjectAS71Server.class,
		HotDeployJSPFileAS71Server.class,
		UndeployJSPProjectAS71Server.class,
		DeleteServerAS71Server.class,
		
		CreateAS70Server.class,
		OperateAS70Server.class,
		DeployJSPProjectAS70Server.class,
		HotDeployJSPFileAS70Server.class,
		UndeployJSPProjectAS70Server.class,
		DeleteServerAS70Server.class,
		
		CreateAS6Server.class,
		OperateAS6Server.class,
		DeployJSPProjectAS6Server.class,
		HotDeployJSPFileAS6Server.class,
		UndeployJSPProjectAS6Server.class,
		DeleteServerAS6Server.class,
})
public class StableTestsSuite {

}
