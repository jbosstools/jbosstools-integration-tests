package org.jboss.tools.portlet.ui.bot.test.task.dialog;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.portlet.ui.bot.test.task.AbstractSWTTask;

/**
 * Checks if there is a dialog asking if the associated perspective 
 * should be open and if so, clicks Yes or No button depending on the 
 * constructor argument. 
 *  
 * @author ljelinko
 *
 */
public class AssociatedPerspectiveDialogClosingTask extends AbstractSWTTask {

	private boolean open;

	public AssociatedPerspectiveDialogClosingTask(boolean open) {
		this.open = open;
	}

	@Override
	public void perform() {
		
		SWTBot bot;
		
		try {
			bot = getBot().shell("Open Associated Perspective?").activate().bot();
		} catch (WidgetNotFoundException e) {
			// dialog is not open, skip the rest
			return;
		}
		
		if (open){
			bot.button("Yes").click();
		} else {
			bot.button("No").click();
		}
	}
}
