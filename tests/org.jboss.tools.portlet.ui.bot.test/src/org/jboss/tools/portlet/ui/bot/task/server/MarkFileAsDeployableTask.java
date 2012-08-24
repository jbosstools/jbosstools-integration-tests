package org.jboss.tools.portlet.ui.bot.task.server;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

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

	private WorkspaceFile workspaceFile;

	public MarkFileAsDeployableTask(WorkspaceFile file) {
		this.workspaceFile = file;
	}

	@Override
	public void perform() {
		performInnerTask(new FileContextMenuSelectingTask(workspaceFile, "Mark as Deployable"));

		SWTBotFactory.getBot().waitUntil(shellIsActive("Really mark these resources as deployable?"));

		// for the confirmation dialog select OK (the dialog is native and normal swtbot functions do now work)
		try {
			Robot robot = new Robot();
			if (!isWindowsOS()){
				robot.keyPress(KeyEvent.VK_RIGHT);
				robot.keyRelease(KeyEvent.VK_RIGHT);
			} else {
				robot.keyPress(KeyEvent.VK_RIGHT);
				robot.keyRelease(KeyEvent.VK_RIGHT);
				robot.keyPress(KeyEvent.VK_LEFT);
				robot.keyRelease(KeyEvent.VK_LEFT);
			}
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			throw new IllegalStateException("Cannot create instance of " + Robot.class + " in order to close native dialog", e);
		}
	}

	private boolean isWindowsOS(){
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
}
