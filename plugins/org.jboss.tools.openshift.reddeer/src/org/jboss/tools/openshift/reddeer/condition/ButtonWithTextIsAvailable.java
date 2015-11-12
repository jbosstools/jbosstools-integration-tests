package org.jboss.tools.openshift.reddeer.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;

public class ButtonWithTextIsAvailable implements WaitCondition {

	private String buttonText;

	public ButtonWithTextIsAvailable(String buttonText) {
		this.buttonText = buttonText;
	}

	@Override
	public boolean test() {
		try {
			new PushButton(buttonText);
			return true;
		} catch (CoreLayerException ex) {
			return false;
		}
	}

	@Override
	public String description() {
		return " button with text " + buttonText + " is available";
	}

}