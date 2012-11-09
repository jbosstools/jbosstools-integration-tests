package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;

public class ButtonIsEnabled implements WaitCondition{
	
	private String text;
    
    public ButtonIsEnabled(String text){
    	this.text = text;
    }
	@Override
	public boolean test() {
		return new PushButton(text).isEnabled();
	}

	@Override
	public String description() {
		return null;
	}

}
