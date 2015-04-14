package org.jboss.tools.arquillian.ui.bot.reddeer.junit.view;

import org.jboss.reddeer.eclipse.jdt.ui.junit.JUnitView;
import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;

/**
 * Checks if there is running test in JUnit view. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JUnitTestIsRunningCondition implements WaitCondition {

	private JUnitView view;
	
	public JUnitTestIsRunningCondition() {
		view = new JUnitView();
		view.open();
	}
	
	@Override
	public boolean test() {
		view.activate();
		return new DefaultToolItem("Stop JUnit Test Run").isEnabled();
	}

	@Override
	public String description() {
		return "at least one JUnit test is running";
	}
}
