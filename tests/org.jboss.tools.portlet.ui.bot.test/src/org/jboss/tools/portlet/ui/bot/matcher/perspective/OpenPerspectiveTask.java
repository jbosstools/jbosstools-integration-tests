package org.jboss.tools.portlet.ui.bot.matcher.perspective;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
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
		SWTBotFactory.getOpen().perspective(perspective);
	}
}
