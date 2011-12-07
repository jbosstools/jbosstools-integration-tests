package org.jboss.tools.portlet.ui.bot.task.console;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * Clears the console. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ConsoleClearingTask extends AbstractSWTTask {

	@Override
	public void perform() {
		// does not use the ConsoleView.clear() funtionality because it didn't work for me 
		SWTBotView view = SWTBotFactory.getConsole().show();
		
		for (SWTBotToolbarButton button : view.getToolbarButtons()){
			if (IDELabel.ConsoleView.BUTTON_CLEAR_CONSOLE_TOOLTIP.equals(button.getToolTipText())){
				button.click();
			}
		}
	}
}
