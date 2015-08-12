package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
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
