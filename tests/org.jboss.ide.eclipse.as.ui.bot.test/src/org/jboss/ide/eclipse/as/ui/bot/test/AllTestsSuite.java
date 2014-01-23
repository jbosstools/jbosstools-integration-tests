package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.as3.CreateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.DeleteServerAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.DeployJSPProjectAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.HotDeployJSPFileAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.OperateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.UndeployJSPProjectAS3Server;
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
import org.jboss.ide.eclipse.as.ui.bot.test.as71.ServerStateDetectorsAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.UndeployJSPProjectAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.CreateEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.DeleteServerEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.DeployJSPProjectEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.HotDeployJSPFileEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.OperateEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.UndeployJSPProjectEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.CreateEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.DeleteServerEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.DeployJSPProjectEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.HotDeployJSPFileEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.OperateEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.UndeployJSPProjectEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.CreateEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.DeleteServerEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.DeployJSPProjectEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.HotDeployJSPFileEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.OperateEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.UndeployJSPProjectEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.CreateEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.DeleteServerEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.DeployJSPProjectEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.HotDeployJSPFileEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.OperateEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.ServerStateDetectorsEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.UndeployJSPProjectEAP6xServer;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateEAP6xServer.class,
		CreateEAP60Server.class,
		CreateEAP5Server.class,
		CreateEAP4Server.class,
		CreateAS71Server.class,
		CreateAS70Server.class,
		CreateAS6Server.class,
		CreateAS51Server.class,
		CreateAS50Server.class,
		CreateAS42Server.class,
		CreateAS40Server.class,
		CreateAS3Server.class,
//		CreateWildfly8Server.class, need java 7
		
		OperateEAP6xServer.class,
		OperateEAP60Server.class,
		OperateEAP5Server.class,
		OperateEAP4Server.class,
		OperateAS71Server.class,
		OperateAS70Server.class,
		OperateAS6Server.class,
		OperateAS51Server.class,
		OperateAS50Server.class,
		OperateAS42Server.class,
		OperateAS3Server.class,
		OperateAS40Server.class,
//		OperateWildfly8Server.class,

		ServerStateDetectorsEAP6xServer.class,
		ServerStateDetectorsAS71Server.class,
//		ServerStateDetectorsWildfly8Server.class,
		
		DeployJSPProjectEAP6xServer.class,
		DeployJSPProjectEAP60Server.class,
		DeployJSPProjectEAP5Server.class,
		DeployJSPProjectEAP4Server.class,
		DeployJSPProjectAS71Server.class,
		DeployJSPProjectAS70Server.class,
		DeployJSPProjectAS6Server.class,
		DeployJSPProjectAS51Server.class,
		DeployJSPProjectAS50Server.class,
		DeployJSPProjectAS42Server.class,
		DeployJSPProjectAS3Server.class,
		DeployJSPProjectAS40Server.class,
//		DeployJSPProjectWildfly8Server.class,
		
		HotDeployJSPFileEAP6xServer.class,
		HotDeployJSPFileEAP60Server.class,
		HotDeployJSPFileEAP5Server.class,
		HotDeployJSPFileEAP4Server.class,
		HotDeployJSPFileAS71Server.class,
		HotDeployJSPFileAS70Server.class,
		HotDeployJSPFileAS6Server.class,
		HotDeployJSPFileAS51Server.class,
		HotDeployJSPFileAS50Server.class,
		HotDeployJSPFileAS42Server.class,
		HotDeployJSPFileAS40Server.class,
		HotDeployJSPFileAS3Server.class,
//		HotDeployJSPFileWildfly8Server.class,
		
		UndeployJSPProjectEAP6xServer.class,
		UndeployJSPProjectEAP60Server.class,
		UndeployJSPProjectEAP5Server.class,
		UndeployJSPProjectEAP4Server.class,
		UndeployJSPProjectAS71Server.class,
		UndeployJSPProjectAS70Server.class,
		UndeployJSPProjectAS6Server.class,
		UndeployJSPProjectAS51Server.class,
		UndeployJSPProjectAS50Server.class,
		UndeployJSPProjectAS42Server.class,
		UndeployJSPProjectAS3Server.class,
		UndeployJSPProjectAS40Server.class,
//		UndeployJSPProjectWildfly8Server.class,
		
		DeleteServerEAP6xServer.class,
		DeleteServerEAP60Server.class,
		DeleteServerEAP5Server.class,
		DeleteServerEAP4Server.class,
		DeleteServerAS71Server.class,
		DeleteServerAS70Server.class,
		DeleteServerAS6Server.class,
		DeleteServerAS51Server.class,
		DeleteServerAS50Server.class,
		DeleteServerAS42Server.class,
		DeleteServerAS40Server.class,
		DeleteServerAS3Server.class,
//		DeleteServerWildfly8Server.class
})
public class AllTestsSuite {

}
