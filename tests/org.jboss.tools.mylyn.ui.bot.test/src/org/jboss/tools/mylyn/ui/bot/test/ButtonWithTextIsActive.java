package org.jboss.tools.mylyn.ui.bot.test;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
/**
 * Condition is fulfilled when button with text is active
 * @author Vlado Pakan / Len DiMaggio
 *
 */
public class ButtonWithTextIsActive extends AbstractWaitCondition {
	
    private String text;
    
    public ButtonWithTextIsActive(String text){
    	this.text = text;
 	}

	@Override
	public boolean test() {
		
		if (new PushButton(text).isEnabled() ) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public String description() {
		return "Button with text " + text + " is active";
	}

}
