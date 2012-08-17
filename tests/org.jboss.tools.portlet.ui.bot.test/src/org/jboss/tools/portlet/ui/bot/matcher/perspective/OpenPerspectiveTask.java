package org.jboss.tools.portlet.ui.bot.matcher.perspective;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.widgetIsEnabled;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
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

	private static final Logger log = Logger.getLogger(OpenPerspectiveTask.class);
	
	private IPerspective perspective;
	
	public OpenPerspectiveTask(IPerspective name) {
		super();
		this.perspective = name;
	}

	@Override
	public void perform() {
		log.info("Opening wizard");
		log.info("All shells: ");
		for (SWTBotShell shell : SWTBotFactory.getBot().shells()){
			log.info(shell.getText() + ": " + shell);
			log.info("Is active: " + shell.isActive());
		}
		getBot().waitUntil(widgetIsEnabled(getBot().menu("Window")), TaskDuration.NORMAL.getTimeout());
		log.info("Opening wizard");
		log.info("All shells: ");
		for (SWTBotShell shell : SWTBotFactory.getBot().shells()){
			log.info(shell.getText() + ": " + shell);
			log.info("Is active: " + shell.isActive());
		}
		SWTBotFactory.getOpen().perspective(perspective);
	}
	
	@Override
	public SWTBot getBot() {
		return SWTBotFactory.getBot();
	}
}
