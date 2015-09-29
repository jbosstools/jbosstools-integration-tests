package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.products;

import org.jboss.ide.eclipse.as.ui.bot.test.eap7.CreateEAP7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap7.DeleteServerEAP7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap7.DeployJSPProjectEAP7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap7.HotDeployJSPFileEAP7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap7.OperateEAP7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap7.ServerStateDetectorsEAP7Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap7.UndeployJSPProjectEAP7Server;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateEAP7Server.class,
		OperateEAP7Server.class,
		ServerStateDetectorsEAP7Server.class,
		DeployJSPProjectEAP7Server.class,
		HotDeployJSPFileEAP7Server.class,
		UndeployJSPProjectEAP7Server.class,
		DeleteServerEAP7Server.class,
})
public class EAP7TestsSuite {

}
