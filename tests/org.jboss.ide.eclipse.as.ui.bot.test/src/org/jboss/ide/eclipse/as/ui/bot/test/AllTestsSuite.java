package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.as6.CreateAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.DeployJSPProjectAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.OperateAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as6.UndeployJSPProjectAS6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.CreateAS7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.DeployJSPProjectAS7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.HotDeployJSPFile;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.OperateAS7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as7.UndeployJSPProjectAS7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.DeleteServer;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateAS7Server.class, 
		CreateAS6Server.class,
		OperateAS7Server.class,
		OperateAS6Server.class,
		DeployJSPProjectAS7Server.class,
		DeployJSPProjectAS6Server.class,
		HotDeployJSPFile.class,
		UndeployJSPProjectAS7Server.class,
		UndeployJSPProjectAS6Server.class,
		DeleteServer.class
})
public class AllTestsSuite {

}
