package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.products;

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
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateEAP6xServer.class,
		CreateEAP60Server.class,
		CreateEAP5Server.class,
		CreateEAP4Server.class,
		
		OperateEAP6xServer.class,
		OperateEAP60Server.class,
		OperateEAP5Server.class,
		OperateEAP4Server.class,

		ServerStateDetectorsEAP6xServer.class,
		
		DeployJSPProjectEAP6xServer.class,
		DeployJSPProjectEAP60Server.class,
		DeployJSPProjectEAP5Server.class,
		DeployJSPProjectEAP4Server.class,
		
		HotDeployJSPFileEAP6xServer.class,
		HotDeployJSPFileEAP60Server.class,
		HotDeployJSPFileEAP5Server.class,
		HotDeployJSPFileEAP4Server.class,
		
		UndeployJSPProjectEAP6xServer.class,
		UndeployJSPProjectEAP60Server.class,
		UndeployJSPProjectEAP5Server.class,
		UndeployJSPProjectEAP4Server.class,
		
		DeleteServerEAP6xServer.class,
		DeleteServerEAP60Server.class,
		DeleteServerEAP5Server.class,
		DeleteServerEAP4Server.class,
})
public class ProductsTestsSuite {

}
