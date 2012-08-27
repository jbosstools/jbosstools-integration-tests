package org.jboss.tools.portlet.ui.bot.task.server;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;

import org.apache.log4j.Logger;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.workspace.FileContextMenuSelectingTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

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
		performInnerTask(new FileContextMenuSelectingTask(workspaceFile, "Mark as Deployable"));

		log.info("Waiting for confirmation shell to appear");
		SWTBotFactory.getBot().waitUntil(shellIsActive("Really mark these resources as deployable?"));

		getBot().button("OK").click();
		
		log.info("Waiting for confirmation shell to disappear");
		SWTBotFactory.getBot().waitWhile(shellIsActive("Really mark these resources as deployable?"));
	}
}
