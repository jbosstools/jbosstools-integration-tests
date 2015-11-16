package org.jboss.tools.usercase.ticketmonster.ui.bot.test.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.forge.ForgeConsole;

public class ForgeContainsText extends AbstractWaitCondition {
	
	private ForgeConsole console;
	private String text;
	
	public ForgeContainsText(ForgeConsole console,String text){
		this.console = console;
		this.text= text;
	}

	@Override
	public boolean test() {
		return console.getText().contains(text);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

}
