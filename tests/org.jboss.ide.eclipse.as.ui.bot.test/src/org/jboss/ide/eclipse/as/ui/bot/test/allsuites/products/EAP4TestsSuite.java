package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.products;

import org.jboss.ide.eclipse.as.ui.bot.test.eap4.CreateEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.DeleteServerEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.DeployJSPProjectEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.HotDeployJSPFileEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.OperateEAP4Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap4.UndeployJSPProjectEAP4Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateEAP4Server.class,
		OperateEAP4Server.class,
		DeployJSPProjectEAP4Server.class,
		HotDeployJSPFileEAP4Server.class,
		UndeployJSPProjectEAP4Server.class,
		DeleteServerEAP4Server.class,
})
public class EAP4TestsSuite {

}
