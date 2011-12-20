package org.jboss.tools.portlet.ui.bot.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.StringDescription;

/**
 * Custom implementation of {@link MatcherAssert} that allows to customize the error message. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class WorkspaceAssert {

	public static void assertThatInWorkspace(Matcher<Void> matcher) {
		assertThatInWorkspace("", matcher);
	}

	public static void assertThatInWorkspace(String reason, Matcher<Void> matcher) {
		if (!matcher.matches(null)) {
			Description description = new StringDescription();
			description.appendText(reason)
			.appendText("\nExpected that ")
			.appendDescriptionOf(matcher)
			.appendText("\n");

			throw new java.lang.AssertionError(description.toString());
		}
	}

	public static <T> void assertThatInWorkspace(T actual, Matcher<T> matcher) {
		assertThatInWorkspace("", actual, matcher);
	}

	public static <T> void assertThatInWorkspace(String reason, T actual, Matcher<T> matcher) {
		if (!matcher.matches(actual)) {
			Description description = new StringDescription();
			description.appendText(reason)
			.appendText("\nExpected that ")
			.appendValue(actual)
			.appendText(" ")
			.appendDescriptionOf(matcher)
			.appendText("\n");

			throw new java.lang.AssertionError(description.toString());
		}
	}
}
