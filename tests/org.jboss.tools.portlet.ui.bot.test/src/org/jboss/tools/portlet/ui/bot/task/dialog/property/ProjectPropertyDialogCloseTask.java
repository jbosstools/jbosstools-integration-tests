package org.jboss.tools.portlet.ui.bot.task.dialog.property;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;

public class ProjectPropertyDialogCloseTask extends AbstractSWTTask {

	@Override
	public void perform() {
		SWTBotShell activeShell = getBot().activeShell();
		getBot().button("OK").click();
		getBot().waitUntil(shellCloses(activeShell), TaskDuration.NORMAL.getTimeout());
		new WaitWhile(new JobIsRunning());
	}
}
