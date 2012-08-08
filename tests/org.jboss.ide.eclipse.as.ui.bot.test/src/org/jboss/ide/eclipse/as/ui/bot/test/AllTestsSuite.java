package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.as4.CreateAS4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as4.DeployJSPProjectAS4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as4.OperateAS4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as4.UndeployJSPProjectAS4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as5.CreateAS5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as5.DeployJSPProjectAS5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as5.OperateAS5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as5.UndeployJSPProjectAS5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.CreateAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.DeployJSPProjectAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.OperateAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.UndeployJSPProjectAS6Server;
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
		CreateAS6Server.class,
		CreateAS5Server.class,
		CreateAS4Server.class,
		OperateEAP6Server.class,
		OperateAS6Server.class,
		OperateAS5Server.class,
		OperateAS4Server.class,
		DeployJSPProjectEAP6Server.class,
		DeployJSPProjectAS6Server.class,
		DeployJSPProjectAS5Server.class,
		DeployJSPProjectAS4Server.class,
		HotDeployJSPFile.class,
		UndeployJSPProjectEAP6Server.class,
		UndeployJSPProjectAS6Server.class,
		UndeployJSPProjectAS5Server.class,
		UndeployJSPProjectAS4Server.class,
		DeleteServer.class
})
public class AllTestsSuite {

}
