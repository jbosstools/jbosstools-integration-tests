package org.jboss.tools.portlet.ui.bot.task.server;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.workspace.FileContextMenuSelectingTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.logging.WidgetsLogger;

/**
 * Marks a file as deployable for the specified server (it works only if there is just one server defined) 
 * 
 * @author Lucia Jelinkova
 *
 */
public class MarkFileAsDeployableTask extends AbstractSWTTask {

	private static final Logger log = Logger.getLogger(MarkFileAsDeployableTask.class);
	
	private WorkspaceFile workspaceFile;

	public MarkFileAsDeployableTask(WorkspaceFile file) {
		this.workspaceFile = file;
	}

	@Override
	public void perform() {
		log.info("Marking " + workspaceFile.getFileName() + " as deployable");
		log.info("Logging all shells: ");
		for (SWTBotShell shell : SWTBotFactory.getBot().shells()){
			log.info("Shell: " + shell.getText() + ", is active: " + shell.isActive());
		}
		performInnerTask(new FileContextMenuSelectingTask(workspaceFile, "Mark as Deployable"));

		log.info("Waiting for confirmation shell to appear");
		log.info("Logging all shells: ");
		for (SWTBotShell shell : SWTBotFactory.getBot().shells()){
			log.info("Shell: " + shell.getText() + ", is active: " + shell.isActive());
		}
		SWTBotFactory.getBot().waitUntil(shellIsActive("Really mark these resources as deployable?"));
		SWTBotFactory.getBot().shell("Really mark these resources as deployable?").activate();
		log.info("Logging all widgets:");
		log.info("Logging all shells: ");
		for (SWTBotShell shell : SWTBotFactory.getBot().shells()){
			log.info("Shell: " + shell.getText() + ", is active: " + shell.isActive());
		}
		WidgetsLogger.log();
		getBot().button("OK").click();
		
		log.info("Waiting for confirmation shell to disappear");
		SWTBotFactory.getBot().waitWhile(shellIsActive("Really mark these resources as deployable?"));
	}
}
