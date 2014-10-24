package org.jboss.ide.eclipse.as.ui.bot.test.allsuites.products;

import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.CreateEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.DeleteServerEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.DeployJSPProjectEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.HotDeployJSPFileEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.OperateEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.ServerStateDetectorsEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.UndeployJSPProjectEAP6xServer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		CreateEAP6xServer.class,
		OperateEAP6xServer.class,
		ServerStateDetectorsEAP6xServer.class,
		DeployJSPProjectEAP6xServer.class,
		HotDeployJSPFileEAP6xServer.class,
		UndeployJSPProjectEAP6xServer.class,
		DeleteServerEAP6xServer.class,
})
public class EAP6xTestsSuite {

}
