package org.jboss.tools.openshift.ui.bot.test.condition;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class ConsoleHasText implements WaitCondition{

	public ConsoleHasText() {
	}
	
	@Override
	public boolean test() {
		return !new ConsoleView().getConsoleText().isEmpty();
	}

	@Override
	public String description() {
		return "console contains text";
	}

	
}
