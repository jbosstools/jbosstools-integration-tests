package org.jboss.tools.ws.reddeer.swt.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.tools.ui.bot.ext.view.ConsoleView;

/**
 * Condition is fulfilled when console's output contains required text
 * 
 * @author rrabara
 *
 */
public class ConsoleContainsText implements WaitCondition {
	private String requiredText;
	private String consoleText;
	private ConsoleView console;
	
	public ConsoleContainsText(String requiredText, ConsoleView console) {
		if(requiredText == null)
			throw new NullPointerException("requiredText");
		if(requiredText.length() == 0)
			throw new IllegalArgumentException("requiredText is empty!");
		if(console == null)
			throw new NullPointerException("console");
		this.requiredText = requiredText;
		this.console = console;			
	}
	
	@Override
	public boolean test() {
		consoleText = console.getConsoleText();
		return consoleText.contains(requiredText);
	}

	@Override
	public String description() {
		return "Console doesn't contains required text("+requiredText+").\nConsole output: "+consoleText;
	}
	
	public String getConsoleText() {
		return console.getConsoleText();
	}
}
