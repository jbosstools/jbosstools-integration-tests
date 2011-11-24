package org.jboss.tools.portlet.ui.bot.task.dialog;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

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
			for (final Shell s : getBot().getFinder().getShells()){
				UIThreadRunnable.asyncExec(getBot().getDisplay(), new VoidResult(){

					@Override
					public void run() {
						System.out.println(s.getText());
						System.out.println(s);
					}
			});
			}
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
