package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.as3.CreateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.DeployJSPProjectAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.OperateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.UndeployJSPProjectAS3Server;
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
import org.jboss.ide.eclipse.as.ui.bot.test.as7.CreateAS7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.DeployJSPProjectAS7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.OperateAS7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.UndeployJSPProjectAS7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeleteServer;
import org.jboss.ide.eclipse.as.ui.bot.test.template.HotDeployJSPFile;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateAS7Server.class, 
		CreateAS6Server.class,
		CreateAS5Server.class,
		CreateAS4Server.class,
		CreateAS3Server.class,
		OperateAS7Server.class,
		OperateAS6Server.class,
		OperateAS5Server.class,
		OperateAS4Server.class,
		OperateAS3Server.class,
		DeployJSPProjectAS7Server.class,
		DeployJSPProjectAS6Server.class,
		DeployJSPProjectAS5Server.class,
		DeployJSPProjectAS4Server.class,
		DeployJSPProjectAS3Server.class,
		HotDeployJSPFile.class,
		UndeployJSPProjectAS7Server.class,
		UndeployJSPProjectAS6Server.class,
		UndeployJSPProjectAS5Server.class,
		UndeployJSPProjectAS4Server.class,
		UndeployJSPProjectAS3Server.class,
		DeleteServer.class
})
public class AllTestsSuite {

}
