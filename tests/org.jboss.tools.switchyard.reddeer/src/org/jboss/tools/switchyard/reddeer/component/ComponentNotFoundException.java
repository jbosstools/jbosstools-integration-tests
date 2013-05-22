package org.jboss.tools.switchyard.reddeer.component;

import org.hamcrest.Matcher;

/**
 * 
 * @author apodhrad
 * 
 */
public class ComponentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1532558678103406801L;

	public ComponentNotFoundException(String tooltip) {
		super("Couldn't find a component with tooltip '" + tooltip + "'");
	}

	public ComponentNotFoundException(Matcher<?> matcher, int index) {
		super("Couldn't find a component " + matcher + " with index " + index);
	}

}
