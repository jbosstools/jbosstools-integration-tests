package org.jboss.tools.forge.reddeer.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.tools.forge.reddeer.view.ForgeConsoleView;

/**
 * Returns true if the console contains a given text
 * 
 * @author psrna
 * 
 */
public class ForgeConsoleHasText implements WaitCondition {
	
	private String text;

	/**
	 * Construct the condition with a given text.
	 * 
	 * @param text Text
	 */
	public ForgeConsoleHasText(String text) {
		this.text = text;
	}

	@Override
	public boolean test() {
		String consoleText = getConsoleText();
		return consoleText.contains(text);
	}

	@Override
	public String description() {
		String consoleText = getConsoleText();
		return "console contains '" + text + "'\n" + consoleText;
	}

	private static String getConsoleText() {
		ForgeConsoleView forgeConsoleView = new ForgeConsoleView();
		forgeConsoleView.open();
		return forgeConsoleView.getConsoleText();
	}
}
