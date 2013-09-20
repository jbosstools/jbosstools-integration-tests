package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.widget.ContextButton;

/**
 * 
 * @author apodhrad
 * 
 */
public class ContextButtonAppeared implements WaitCondition {

	private Component component;
	private String label;

	public ContextButtonAppeared(Component component, String label) {
		this.component = component;
		this.label = label;
	}

	@Override
	public boolean test() {
		component.hover();
		try {
			new ContextButton(label);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String description() {
		return "Context button '" + label + "' didn't appear!";
	}
}
