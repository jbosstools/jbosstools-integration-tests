package org.jboss.tools.maven.ui.bot.test.utils;

import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.common.condition.AbstractWaitCondition;

public class ProjectHasErrors extends AbstractWaitCondition {

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
			for (Problem problem : pv.getProblems(ProblemType.ERROR)) {
				if (problem.getPath().contains(projectName)
						&& problem.getType().contains(acceptType)) {
					continue;
				} else if (problem.getPath().contains(projectName)) {
					return true;
				}
			}
		} else {
			for (Problem problem : pv.getProblems(ProblemType.ERROR)) {
				if (problem.getPath().contains(projectName)) {
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
