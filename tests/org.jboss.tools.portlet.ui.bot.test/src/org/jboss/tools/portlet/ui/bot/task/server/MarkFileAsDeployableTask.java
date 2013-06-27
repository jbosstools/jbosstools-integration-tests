package org.jboss.tools.portlet.ui.bot.task.server;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

/**
 * Marks a file as deployable for the specified server (it works only if there is just one server defined) 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
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
		new PackageExplorer().getProject(workspaceFile.getProject()).getProjectItem(workspaceFile.getFilePathAsArray()).select();
		new ContextMenu("Mark as Deployable").select();
		new PushButton("OK").click();
	}
}

