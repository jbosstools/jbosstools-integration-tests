package org.jboss.tools.ws.reddeer.swt.condition;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.AbstractProblemMatcher;

/**
 * Wait condition expects existence of specific amount of problems in Problems view.
 * 
 * @author Radoslav Rabara, mlabuda@redhat.com
 */
public class ExactNumberOfProblemsExists implements WaitCondition {
	
	private AbstractProblemMatcher[] matchers;
	private ProblemType problemType;
	private int expectedProblemsCount;
	
	private List<Problem> problems = new ArrayList<Problem>();
	
	/**
	 * Constructs the condition for the specified problem <var>type</var> and
	 * the specified <var>count</var> of the problems.
	 * 
	 * @param type type of the problems
	 * @param count number of the problems
	 */
	public ExactNumberOfProblemsExists(ProblemType type, int count) {
		if(type == null) {
			throw new NullPointerException("Problem Type cannot be null");
		}
		if(count < 0) {
			throw new IllegalArgumentException("Number of problems must be greater than zero.");
		}
		
		this.problemType = type;
		this.expectedProblemsCount = count;
		matchers = new AbstractProblemMatcher[]{};

		new ProblemsView().open();
	}

	/**
	 * Constructs the condition for the specified problem <var>type</var> and
	 * the specified <var>count</var> of the problems.
	 * 
	 * @param type type of the problems
	 * @param count number of the problems
	 * @param matchers problem matchers
	 */
	public ExactNumberOfProblemsExists(ProblemType problemType, int count, AbstractProblemMatcher... matchers) {
		this(problemType, count);
		this.matchers = matchers;
	}

	@Override
	public boolean test() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();

		List<Problem> problems = problemsView.getProblems(problemType, matchers);
		
		return problems.size() == expectedProblemsCount;
	}

	@Override
	public String description() {
		return "there is/are " + problems.size() + " problems in Problems view.\n"
				+ "Expected number of problems is: " + problems.size();
	}
}
