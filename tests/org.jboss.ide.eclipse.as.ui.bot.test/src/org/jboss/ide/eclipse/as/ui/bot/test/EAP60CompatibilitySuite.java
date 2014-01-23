package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.eap60.CreateEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.DeleteServerEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.DeployJSPProjectEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.HotDeployJSPFileEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.OperateEAP60Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap60.UndeployJSPProjectEAP60Server;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateEAP60Server.class, 
		OperateEAP60Server.class,
		DeployJSPProjectEAP60Server.class,
		HotDeployJSPFileEAP60Server.class,
		UndeployJSPProjectEAP60Server.class,
		DeleteServerEAP60Server.class
})
public class EAP60CompatibilitySuite {

}
