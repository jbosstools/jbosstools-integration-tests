package org.jboss.tools.usercase.ticketmonster.ui.bot.test.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;

public class ForgeIsRunning implements WaitCondition{

	@Override
	public boolean test() {
		try{
			new DefaultLabel("Please wait while Forge is starting.*");
		} catch(SWTLayerException ex){
			return false;
		}
		return true;
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

}
