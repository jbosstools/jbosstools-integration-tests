package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.projects;

import org.jboss.ide.eclipse.as.ui.bot.test.as3.CreateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.DeleteServerAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.DeployJSPProjectAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.HotDeployJSPFileAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.OperateAS3Server;
import org.jboss.ide.eclipse.as.ui.bot.test.as3.UndeployJSPProjectAS3Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateAS3Server.class,
		OperateAS3Server.class,
		DeployJSPProjectAS3Server.class,
		HotDeployJSPFileAS3Server.class,
		UndeployJSPProjectAS3Server.class,
		DeleteServerAS3Server.class,
})
public class AS3TestsSuite {

}
