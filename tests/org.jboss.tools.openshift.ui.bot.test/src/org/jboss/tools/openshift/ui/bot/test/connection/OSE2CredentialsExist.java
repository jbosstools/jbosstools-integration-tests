package org.jboss.tools.openshift.ui.bot.test.connection;

import org.jboss.reddeer.junit.execution.TestMethodShouldRun;
import org.junit.runners.model.FrameworkMethod;

public class OSE2CredentialsExist implements TestMethodShouldRun {

	@Override
	public boolean shouldRun(FrameworkMethod method) {
		return System.getProperty("openshift.xserver") != null;
	}
}
