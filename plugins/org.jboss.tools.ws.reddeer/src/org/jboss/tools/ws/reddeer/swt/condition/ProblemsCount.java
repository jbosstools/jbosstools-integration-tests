package org.jboss.tools.ws.reddeer.swt.condition;

import java.util.List;

import org.hamcrest.Matcher;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WaitCondition;

/**
 * Returns true if the {@link ProblemsView} contains the specified number of
 * the specified {@link ProblemType}.
 * 
 * @author Radoslav Rabara
 */
public class ProblemsCount implements WaitCondition {
	public enum ProblemType {
		ERROR, WARNING;
	}

	private ProblemType problemType;
	private int problemsCount;

	private boolean filterProblems = false;
	private Matcher<String> description;
	private Matcher<String> resource;
	private Matcher<String> path;
	private Matcher<String> location;
	private Matcher<String> type;

	private List<TreeItem> problems;

	/**
	 * Constructs the condition for the specified problem <var>type</var> and
	 * the specified <var>count</var> of the problems.
	 * 
	 * @param type type of the problems that the condition will looking for
	 * @param count number of the problems
	 */
	public ProblemsCount(ProblemType type, int count) {
		if(type == null) {
			throw new NullPointerException("type");
		}
		if(problemType != ProblemType.WARNING && problemType != ProblemType.ERROR) {
			new IllegalArgumentException("Unknown ProblemType" + type);
		}
		if(count < 0) {
			throw new IllegalArgumentException("count must be a positive number or a zero");
		}
		this.problemType = type;
		this.problemsCount = count;

		new ProblemsView().open();
	}

	/**
	 * Constructs the condition for the specified problem <var>type</var> and
	 * the specified <var>count</var> of the problems.
	 * 
	 * @param type type of the problems that the condition will looking for
	 * @param count number of the problems
	 * @param description filter by description
	 * @param resource filter by resource
	 * @param path filter by path
	 * @param location filter by location
	 * @param type filter by type
	 */
	public ProblemsCount(ProblemType problemType, int count,
			Matcher<String> description, Matcher<String> resource,
			Matcher<String> path, Matcher<String> location,
			Matcher<String> type) {
		this(problemType, count);
		this.description = description;
		this.resource = resource;
		this.path = path;
		this.location = location;
		this.type = type;
		this.filterProblems = true;
	}

	@Override
	public boolean test() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();

		switch (problemType) {
		case ERROR:
			if(filterProblems) {
				problems = new ProblemsView()
					.getErrors(description, resource, path, location, type);
			} else {
				problems = problemsView.getAllErrors();
			}
			break;
		case WARNING:
			if(filterProblems) {
				problems = new ProblemsView()
					.getWarnings(description, resource, path, location, type);
			} else {
				problems = problemsView.getAllWarnings();
			}
			break;
			default:
				new UnsupportedOperationException("Unknown Problem type " + problemType);
		}

		return problems.size() == problemsCount;
	}

	@Override
	public String description() {
		return "there are " + (problems != null ? problems.size() : "NULL") + " "
				+ (problemType == ProblemType.ERROR ? "warnings" : "errors")
				+ " in Problems view\nExpected are " + problemsCount + " errors\n";
	}
}
