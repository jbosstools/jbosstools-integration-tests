package org.jboss.tools.switchyard.reddeer.matcher;

import java.lang.reflect.Method;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.IFigure;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Matcher which returns true if a context button contains a given label.
 * 
 * @author apodhrad
 * 
 */
public class ContextButtonMatcher extends BaseMatcher<IFigure> {

	private String label;

	public ContextButtonMatcher(String label) {
		this.label = label;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof Clickable && obj.getClass().toString().endsWith("ContextButton")) {
			String text = null;
			try {
				Method getEntry = obj.getClass().getMethod("getEntry");
				obj = getEntry.invoke(obj);
				Method getText = obj.getClass().getMethod("getText");
				obj = getText.invoke(obj);
				text = (String) obj;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return text != null && text.equals(label);
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("context button with label '" + label + "'");

	}
}
