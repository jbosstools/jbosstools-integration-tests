package org.jboss.tools.switchyard.reddeer.view;

import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.workbench.view.View;

/**
 * JUnit view
 * 
 * @author apodhrad
 *
 */
public class JUnitView extends View {

	public JUnitView() {
		super("Java", "JUnit");
	}

	public String getRuns() {
		open();
		return Bot.get().text(0).getText();
	}

	public String getErrors() {
		open();
		return Bot.get().text(1).getText();
	}

	public String getFailures() {
		open();
		return Bot.get().text(2).getText();
	}
}
