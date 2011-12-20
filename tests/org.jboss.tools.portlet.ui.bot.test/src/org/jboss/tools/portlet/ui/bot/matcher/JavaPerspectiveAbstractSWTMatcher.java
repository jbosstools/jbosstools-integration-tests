package org.jboss.tools.portlet.ui.bot.matcher;

import org.jboss.tools.portlet.ui.bot.matcher.perspective.OpenJavaPerspectiveTask;

/**
 * Matcher that operates in Java Perspective.  
 * 
 * @author Lucia Jelinkova
 *
 * @param <T>
 */
public abstract class JavaPerspectiveAbstractSWTMatcher<T> extends AbstractSWTMatcher<T> {

	protected abstract boolean matchesSafelyInJavaPerspective(T item);
	
	@Override
	public final boolean matchesSafely(T item) {
		performInnerTask(new OpenJavaPerspectiveTask());
		return matchesSafelyInJavaPerspective(item);
	}
}
