package org.jboss.tools.bpel.reddeer.condition;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WaitCondition;

/**
 * Returns true if there is no error
 * 
 * @author apodhrad
 * 
 */
public class NoErrorExists implements WaitCondition {

	private List<TreeItem> errors;

	@Override
	public boolean test() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		errors = problemsView.getAllErrors();
		return errors.isEmpty();
	}

	@Override
	public String description() {
		StringBuffer result = new StringBuffer();
		if (errors != null && errors.size() > 0) {
			result.append("There are the following " + errors.size() + " errors:");
			result.append(System.getProperty("line.separator"));
			for (TreeItem error : errors) {
				result.append(error.getText());
				result.append(System.getProperty("line.separator"));
			}
		} else {
			result.append("Tehere are no errors");
		}
		return result.toString();
	}
}
