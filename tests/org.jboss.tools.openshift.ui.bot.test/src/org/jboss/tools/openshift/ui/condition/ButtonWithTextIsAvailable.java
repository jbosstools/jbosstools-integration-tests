package org.jboss.tools.openshift.ui.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
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
		} catch (SWTLayerException ex) {
			return false;
		}
	}

	@Override
	public String description() {
		return " button with text " + buttonText + " is available";
	}
	
}
