package org.jboss.tools.ws.ui.bot.test.condition;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView;

public class WsTesterNotNullResponseText implements ICondition {

	private WsTesterView wsTesterView;
	
	public WsTesterNotNullResponseText(WsTesterView wsTesterView) {
		this.wsTesterView = wsTesterView;
	}
	
	@Override
	public boolean test() throws Exception {
		return !(wsTesterView.getResponseBody().isEmpty());
	}

	@Override
	public void init(SWTBot bot) {
		// nothing do here

	}

	@Override
	public String getFailureMessage() {
		return "WS Tester View has null response message";
	}

}
