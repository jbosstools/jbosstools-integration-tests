package org.jboss.tools.portlet.ui.bot.test.task.dialog;

import org.jboss.tools.portlet.ui.bot.test.task.AbstractSWTTask;

public class ProjectPropertyDialogCloseTask extends AbstractSWTTask {

	@Override
	public void perform() {
		getBot().button("OK").click();
	}
}
