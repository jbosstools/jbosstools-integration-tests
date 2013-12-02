package org.jboss.tools.ui.bot.ext.config.requirement;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;

public class AddJBPM extends RequirementBase {

	@Override
	public boolean checkFulfilled() {
		return SWTTestExt.configuredState.getJBPM().isConfigured;
	}

	@Override
	public void handle() {
		String jbpmVersion = TestConfigurator.currentConfig.getJBPM().version;
		String jbpmName = "JBPM-" + jbpmVersion;
		
		// jBPM 5 is added elsewhere which means we need to distinguish versions.
		if (jbpmVersion.startsWith("5") || jbpmVersion.startsWith("6")) {
			SWTTestExt.eclipse.addJBPM5Runtime(jbpmName, TestConfigurator.currentConfig.getJBPM().runtimeHome);
		} else {
			SWTTestExt.eclipse.addJBPMRuntime(jbpmName, TestConfigurator.currentConfig.getJBPM().runtimeHome);
		}
		
		SWTTestExt.configuredState.getJBPM().isConfigured=true;
		SWTTestExt.configuredState.getJBPM().name=jbpmName;
		SWTTestExt.configuredState.getJBPM().version=TestConfigurator.currentConfig.getJBPM().version;	

	}

}
