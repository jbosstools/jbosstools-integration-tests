package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.eap5.CreateEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.DeleteServerEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.DeployJSPProjectEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.HotDeployJSPFileEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.OperateEAP5Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap5.UndeployJSPProjectEAP5Server;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(JBTTestSuite.class)
@Suite.SuiteClasses({
		CreateEAP5Server.class, 
		OperateEAP5Server.class,
		DeployJSPProjectEAP5Server.class,
		HotDeployJSPFileEAP5Server.class,
		UndeployJSPProjectEAP5Server.class,
		DeleteServerEAP5Server.class
})
public class EAP5CompatibilitySuite {

}
