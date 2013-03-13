package org.jboss.tools.bpel.ui.bot.ext.condition;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.condition.WaitCondition;

/**
 * Returns true if there is no error
 * 
 * @author apodhrad
 * 
 */
public class NoErrorExists implements WaitCondition {

	@Override
	public boolean test() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		return problemsView.getAllErrors().isEmpty();
	}

	@Override
	public String description() {
		return "There is still at least one error!";
	}

}
