package org.jboss.tools.ui.bot.ext.config.requirement;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;

public class AddSeam extends RequirementBase {

	@Override
	public boolean checkFullfilled() {
		return SWTTestExt.configuredState.getSeam().isConfiured;
	}

	@Override
	public void handle() {
		String seamName = "Seam-"+TestConfigurator.seam.version;
		SWTTestExt.eclipse.addSeamRuntime(seamName, TestConfigurator.seam.version, TestConfigurator.seam.seamHome);
		SWTTestExt.configuredState.getSeam().isConfiured=true;
		SWTTestExt.configuredState.getSeam().name=seamName;
		SWTTestExt.configuredState.getSeam().version=TestConfigurator.seam.version;		
	}

}
