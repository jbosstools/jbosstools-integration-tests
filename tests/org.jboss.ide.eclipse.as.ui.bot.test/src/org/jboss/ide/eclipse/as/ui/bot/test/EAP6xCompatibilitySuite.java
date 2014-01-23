package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.CreateEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.DeleteServerEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.DeployJSPProjectEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.HotDeployJSPFileEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.OperateEAP6xServer;
import org.jboss.ide.eclipse.as.ui.bot.test.eap6x.UndeployJSPProjectEAP6xServer;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateEAP6xServer.class, 
		OperateEAP6xServer.class,
		DeployJSPProjectEAP6xServer.class,
		HotDeployJSPFileEAP6xServer.class,
		UndeployJSPProjectEAP6xServer.class,
		DeleteServerEAP6xServer.class
})

public class EAP6xCompatibilitySuite {
	
	public EAP6xCompatibilitySuite() {
		// TODO Auto-generated constructor stub
	}

}
