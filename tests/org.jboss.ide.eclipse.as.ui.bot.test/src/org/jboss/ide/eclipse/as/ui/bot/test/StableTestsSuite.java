package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.as71.CreateAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.DeleteServerAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.DeployJSPProjectAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.HotDeployJSPFileAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.OperateAS71Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as71.UndeployJSPProjectAS71Server;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateAS71Server.class,
		OperateAS71Server.class,
		DeployJSPProjectAS71Server.class,
		HotDeployJSPFileAS71Server.class,
		UndeployJSPProjectAS71Server.class,
		DeleteServerAS71Server.class,
})
public class StableTestsSuite {

}
