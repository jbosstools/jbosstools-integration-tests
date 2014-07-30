package org.jboss.tools.ui.bot.ext.config.requirement;

import org.jboss.tools.ui.bot.ext.SWTTestExt;

public class RemoveRemoteSystem extends RequirementBase {

	@Override
	public boolean checkFulfilled() {
		return !SWTTestExt.configuredState.getRemoteSystem().isConfigured;
	}

	@Override
	public void handle() {
		String connection = SWTTestExt.configuredState.getRemoteSystem().remoteHost;
		SWTTestExt.eclipse.removeRemoteSystem(connection);
	}

}
