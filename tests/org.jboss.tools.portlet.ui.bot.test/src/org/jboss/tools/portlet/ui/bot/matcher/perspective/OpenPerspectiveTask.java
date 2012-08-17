package org.jboss.tools.portlet.ui.bot.matcher.perspective;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.widgetIsEnabled;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.gen.IPerspective;

/**
 * Opens the given perspective. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class OpenPerspectiveTask extends AbstractSWTTask {

	private IPerspective perspective;
	
	public OpenPerspectiveTask(IPerspective name) {
		super();
		this.perspective = name;
	}

	@Override
	public void perform() {
		getBot().waitUntil(widgetIsEnabled(getBot().menu("Window")), TaskDuration.NORMAL.getTimeout());
		SWTBotFactory.getOpen().perspective(perspective);
	}
	
	@Override
	public SWTBot getBot() {
		return SWTBotFactory.getBot();
	}
}
