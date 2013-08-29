package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;

/**
 * 
 * @author apodhrad
 * 
 */
public class JUnitHasFinished implements WaitCondition {

	@Override
	public boolean test() {
		JUnitView jUnitView = new JUnitView();
		jUnitView.open();
		String status = jUnitView.getViewStatus();
		return status.startsWith("Finished");
	}

	@Override
	public String description() {
		return "JUnit test has not finished yet";
	}
}
