package org.jboss.ide.eclipse.as.ui.bot.test;

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
		OperateAS7Server.class,
		DeployJSPProjectAS7Server.class,
		HotDeployJSPFile.class,
		UndeployJSPProjectAS7Server.class,
		DeleteServer.class
})
public class EAPCompatibilitySuite {

}
