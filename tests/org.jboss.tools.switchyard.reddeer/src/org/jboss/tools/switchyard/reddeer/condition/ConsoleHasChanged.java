package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.wait.AbstractWait;

/**
 * 
 * @author apodhrad
 * 
 */
public class ConsoleHasChanged implements WaitCondition {

	@Override
	public boolean test() {
		ConsoleView consoleView = new ConsoleView();

		consoleView.open();
		String consoleTextBefore = consoleView.getConsoleText();

		AbstractWait.sleep(1000);

		consoleView.open();
		String consoleTextAfter = consoleView.getConsoleText();

		return !consoleTextBefore.equals(consoleTextAfter);
	}

	@Override
	public String description() {
		return "Console is still in work";
	}
}
