package org.jboss.tools.portlet.ui.bot.task.server;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
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

		// for the confirmation dialog select OK (the dialog is native and normal swtbot functions do now work)
		try {
			Robot robot = new Robot();
			if (!isWindowsOS()){
				log.info("Non Windows OS");
				KeyboardFactory.getAWTKeyboard().pressShortcut(Keystrokes.RIGHT, Keystrokes.CR, Keystrokes.LF);
			} else {
				log.info("Windows OS");
				robot.keyPress(KeyEvent.VK_RIGHT);
				robot.keyRelease(KeyEvent.VK_RIGHT);
				robot.keyPress(KeyEvent.VK_LEFT);
				robot.keyRelease(KeyEvent.VK_LEFT);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
			}
		} catch (AWTException e) {
			throw new IllegalStateException("Cannot create instance of " + Robot.class + " in order to close native dialog", e);
		}
		
		log.info("Waiting for confirmation shell to disappear");
		SWTBotFactory.getBot().waitUntil(shellIsActive("Really mark these resources as deployable?"));
	}

	private boolean isWindowsOS(){
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
}
