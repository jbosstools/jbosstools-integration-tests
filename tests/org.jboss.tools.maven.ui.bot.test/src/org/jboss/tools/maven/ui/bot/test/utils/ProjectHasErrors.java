package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.common.condition.WaitCondition;

public class ProjectHasErrors implements WaitCondition {

	String projectName;
	String acceptType;

	public ProjectHasErrors(String projectName, String acceptType) {
		this.projectName = projectName;
		this.acceptType = acceptType;
	}

	@Override
	public boolean test() {
		ProblemsView pv = new ProblemsView();
		pv.open();
		if (acceptType != null) {
			for (TreeItem item : pv.getAllErrors()) {
				if (item.getCell(2).contains(projectName)
						&& item.getCell(4).contains(acceptType)) {
					continue;
				} else if (item.getCell(2).contains(projectName)) {
					return true;
				}
			}
		} else {
			for (TreeItem item : pv.getAllErrors()) {
				if (item.getCell(2).contains(projectName)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String description() {
		return projectName + " contains errors";
	}

}
