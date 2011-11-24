package org.jboss.tools.portlet.ui.bot.task.dialog;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

public class ProjectPropertyDialogCloseTask extends AbstractSWTTask {

	@Override
	public void perform() {
		getBot().button("OK").click();
	}
}
