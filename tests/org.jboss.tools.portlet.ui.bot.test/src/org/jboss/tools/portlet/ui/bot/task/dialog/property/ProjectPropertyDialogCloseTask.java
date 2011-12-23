package org.jboss.tools.portlet.ui.bot.task.dialog.property;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.wait.TaskDuration;
import org.jboss.tools.portlet.ui.bot.task.wait.WaitWhileTask;
import org.jboss.tools.portlet.ui.bot.task.wait.condition.NonSystemJobRunsCondition;
import org.jboss.tools.portlet.ui.bot.task.wait.condition.ShellIsActiveCondition;

public class ProjectPropertyDialogCloseTask extends AbstractSWTTask {

	@Override
	public void perform() {
		SWTBotShell activeShell = getBot().activeShell();
		getBot().button("OK").click();
		performInnerTask(new WaitWhileTask(new ShellIsActiveCondition(activeShell), TaskDuration.NORMAL));
		performInnerTask(new WaitWhileTask(new NonSystemJobRunsCondition(), TaskDuration.LONG));
	}
}
