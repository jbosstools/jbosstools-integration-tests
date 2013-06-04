package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.eap6.CreateEAP6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6.DeleteServerEAP6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6.DeployJSPProjectEAP6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6.HotDeployJSPFileEAP6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6.OperateEAP6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6.UndeployJSPProjectEAP6Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap61.CreateEAP61Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap61.DeleteServerEAP61Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap61.DeployJSPProjectEAP61Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap61.HotDeployJSPFileEAP61Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap61.OperateEAP61Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap61.UndeployJSPProjectEAP61Server;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateEAP6Server.class, 
		OperateEAP6Server.class,
		DeployJSPProjectEAP6Server.class,
		HotDeployJSPFileEAP6Server.class,
		UndeployJSPProjectEAP6Server.class,
		DeleteServerEAP6Server.class,
		CreateEAP61Server.class, 
		OperateEAP61Server.class,
		DeployJSPProjectEAP61Server.class,
		HotDeployJSPFileEAP61Server.class,
		UndeployJSPProjectEAP61Server.class,
		DeleteServerEAP61Server.class
})
public class EAP6CompatibilitySuite {

}
