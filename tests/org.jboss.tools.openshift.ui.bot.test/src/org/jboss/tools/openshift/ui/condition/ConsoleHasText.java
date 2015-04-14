package org.jboss.tools.openshift.ui.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;

/**
 * Condition notifies about not empty console.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ConsoleHasText implements WaitCondition{

	private ConsoleView consoleView;
	
	public ConsoleHasText() {
		consoleView = new ConsoleView();
		consoleView.open();
	}
	
	@Override
	public boolean test() {
		return !consoleView.getConsoleText().isEmpty();
	}

	@Override
	public String description() {
		return "console contains text";
	}

	
}
