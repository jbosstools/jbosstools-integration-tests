package org.jboss.ide.eclipse.as.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class JBTTestSuite extends RedDeerSuite {

	public JBTTestSuite(Class<?> clazz, RunnerBuilder builder) throws InitializationError {
		super(clazz, builder);
	}

	@Override
	public void run(RunNotifier arg0) {
		SWTJBTExt.manageBlockingWidows(false, false);
		super.run(arg0);
	}
}
