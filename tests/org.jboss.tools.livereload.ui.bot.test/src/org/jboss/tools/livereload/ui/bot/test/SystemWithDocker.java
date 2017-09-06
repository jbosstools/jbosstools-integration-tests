package org.jboss.tools.livereload.ui.bot.test;

import org.eclipse.reddeer.common.platform.RunningPlatform;
import org.eclipse.reddeer.junit.execution.TestMethodShouldRun;
import org.junit.runners.model.FrameworkMethod;

public class SystemWithDocker implements TestMethodShouldRun{

	@Override
	public boolean shouldRun(FrameworkMethod method) {
		return RunningPlatform.isLinux();
	}

}
