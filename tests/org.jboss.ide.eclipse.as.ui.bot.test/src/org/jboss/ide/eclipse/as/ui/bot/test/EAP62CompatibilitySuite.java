package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.ide.eclipse.as.ui.bot.test.eap62.CreateEAP62Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap62.DeleteServerEAP62Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap62.DeployJSPProjectEAP62Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap62.HotDeployJSPFileEAP62Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap62.OperateEAP62Server;
import org.jboss.ide.eclipse.as.ui.bot.test.eap62.UndeployJSPProjectEAP62Server;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
		CreateEAP62Server.class, 
		OperateEAP62Server.class,
		DeployJSPProjectEAP62Server.class,
		HotDeployJSPFileEAP62Server.class,
		UndeployJSPProjectEAP62Server.class,
		DeleteServerEAP62Server.class
})

public class EAP62CompatibilitySuite {
	
	public EAP62CompatibilitySuite() {
		// TODO Auto-generated constructor stub
	}

}
