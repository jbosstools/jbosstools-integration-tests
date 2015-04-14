package org.jboss.tools.portlet.ui.bot.task.dialog.property;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

public class ProjectPropertyDialogCloseTask extends AbstractSWTTask {

	@Override
	public void perform() {
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning());
	}
}
