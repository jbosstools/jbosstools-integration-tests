package org.jboss.tools.portlet.ui.bot.matcher;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;

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
		new JavaPerspective().open();
		return matchesSafelyInJavaPerspective(item);
	}
}
