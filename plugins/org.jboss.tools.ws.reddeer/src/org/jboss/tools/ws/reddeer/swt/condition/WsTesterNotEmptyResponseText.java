package org.jboss.tools.ws.reddeer.swt.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;

/**
 * Condition is fulfilled when response body in {@link WsTesterView} is not empty.
 *
 * @author jjankovi
 * @author Radoslav Rabara
 */
public class WsTesterNotEmptyResponseText extends AbstractWaitCondition {

	private WsTesterView wsTesterView = new WsTesterView();

	@Override
	public boolean test() {
		return !(wsTesterView.getResponseBody().isEmpty());
	}

	@Override
	public String description() {
		return "WS Tester View has enoty response message";
	}
}
