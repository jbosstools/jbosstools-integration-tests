package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.as3.CreateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.DeployJSPProjectAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.OperateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.UndeployJSPProjectAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.CreateAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.DeployJSPProjectAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.OperateAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as42.UndeployJSPProjectAS42Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as5.CreateAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as5.DeployJSPProjectAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as5.OperateAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as5.UndeployJSPProjectAS51Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.CreateAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.DeployJSPProjectAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.OperateAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.UndeployJSPProjectAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.CreateAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.DeployJSPProjectAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.OperateAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as70.UndeployJSPProjectAS70Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.CreateAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.DeployJSPProjectAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.OperateAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.UndeployJSPProjectAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.CreateEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.DeployJSPProjectEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.OperateEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.UndeployJSPProjectEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6.CreateEAP6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6.DeployJSPProjectEAP6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6.OperateEAP6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6.UndeployJSPProjectEAP6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeleteServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.HotDeployJSPFile;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateEAP6Server.class,
		CreateEAP5Server.class,
		CreateAS71Server.class,
		CreateAS70Server.class,
		CreateAS6Server.class,
		CreateAS51Server.class,
		CreateAS42Server.class,
		CreateAS3Server.class,
		OperateEAP6Server.class,
		OperateEAP5Server.class,
		OperateAS71Server.class,
		OperateAS70Server.class,
		OperateAS6Server.class,
		OperateAS51Server.class,
		OperateAS42Server.class,
		OperateAS3Server.class,
		DeployJSPProjectEAP6Server.class,
		DeployJSPProjectEAP5Server.class,
		DeployJSPProjectAS71Server.class,
		DeployJSPProjectAS70Server.class,
		DeployJSPProjectAS6Server.class,
		DeployJSPProjectAS51Server.class,
		DeployJSPProjectAS42Server.class,
		DeployJSPProjectAS3Server.class,
		HotDeployJSPFile.class,
		UndeployJSPProjectEAP6Server.class,
		UndeployJSPProjectEAP5Server.class,
		UndeployJSPProjectAS71Server.class,
		UndeployJSPProjectAS70Server.class,
		UndeployJSPProjectAS6Server.class,
		UndeployJSPProjectAS51Server.class,
		UndeployJSPProjectAS42Server.class,
		UndeployJSPProjectAS3Server.class,
		DeleteServer.class
})
public class AllTestsSuite {

}
