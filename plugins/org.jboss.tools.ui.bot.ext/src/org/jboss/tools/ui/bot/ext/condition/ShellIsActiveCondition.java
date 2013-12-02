package org.jboss.tools.ui.bot.ext.condition;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

/**
 * Returns true while the specified shell is active.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class ShellIsActiveCondition implements ICondition {

	private SWTBotShell shell = null;
	private String shellTitle = null;
	private SWTBot bot = null;
	
	public ShellIsActiveCondition(SWTBotShell shell) {
		super();
		this.shell = shell;
	}

	public ShellIsActiveCondition(String shellTitle) {
		super();
		this.shellTitle = shellTitle;
	}
	
	@Override
	public void init(SWTBot bot) {
		this.bot = bot;
	}
	
	@Override
	public boolean test() throws Exception {
		boolean result = false;
		
		if (shell != null) {
			result = shell.isActive();
		}
		else {
			try {
				shell = bot.shell(shellTitle);
				result = shell.isActive();
			} catch (WidgetNotFoundException wnfe){
				result = false;
			}
		}		
		return result;
	}
	
	@Override
	public String getFailureMessage() {
		return "Expected the shell to become active: \"" + shell + "\" is not active!";
	}
}
