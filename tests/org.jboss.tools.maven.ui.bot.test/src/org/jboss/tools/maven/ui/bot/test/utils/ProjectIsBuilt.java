package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class ProjectIsBuilt implements WaitCondition {
	
	@Override
	public boolean test() {
		return new ConsoleView().getConsoleText().contains("BUILD SUCCESS");
	}
	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}
}
