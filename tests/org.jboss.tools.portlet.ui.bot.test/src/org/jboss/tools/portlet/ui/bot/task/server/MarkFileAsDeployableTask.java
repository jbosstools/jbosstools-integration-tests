package org.jboss.tools.portlet.ui.bot.task.server;

import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.workspace.FileContextMenuSelectingTask;

/**
 * Marks a file as deployable for the specified server (it works only if there is just one server defined) 
 * 
 * @author Lucia Jelinkova
 *
 */
public class MarkFileAsDeployableTask extends AbstractSWTTask {

	private WorkspaceFile workspaceFile;

	public MarkFileAsDeployableTask(WorkspaceFile file) {
		this.workspaceFile = file;
	}

	@Override
	public void perform() {
		performInnerTask(new FileContextMenuSelectingTask(workspaceFile, "Mark as Deployable"));
		// for the confirmation dialog select OK (the dialog is native and normal swtbot functions do now work)
		KeyboardFactory.getAWTKeyboard().pressShortcut(Keystrokes.RIGHT);
		KeyboardFactory.getAWTKeyboard().pressShortcut(Keystrokes.CR, Keystrokes.LF);
	}
}
