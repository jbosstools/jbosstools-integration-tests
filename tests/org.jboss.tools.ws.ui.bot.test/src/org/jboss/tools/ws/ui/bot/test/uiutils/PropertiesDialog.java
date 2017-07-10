package org.jboss.tools.ws.ui.bot.test.uiutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.common.reddeer.label.IDELabel;

/**
 * 
 * @author Radoslav Rabara
 *
 */
public class PropertiesDialog {

	private String projectName;

	/**
	 * Opens properties dialog for the specified project.
	 * 
	 * @param projectName name of the project to open properties dialog
	 */
	public void open(String projectName) {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		Project project = projectExplorer.getProject(projectName);
		project.select();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		// Open Project Properties
		assertEquals("Project name", projectName, project.getName());
		assertTrue("Project with name '" + projectName + "' is selected",
				project.isSelected());
	
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new ShellMenu(IDELabel.Menu.PROJECT, IDELabel.Menu.PROPERTIES).select();

		new DefaultShell(IDELabel.Shell.PROPERTIES_FOR + " "
				+ projectName);
		this.projectName = projectName;
	}

	/**
	 * Confirms and closes the dialog.
	 */
	public void finish() {
		finish(TimePeriod.NORMAL);
	}
	
	public void finish(TimePeriod timeout) {
        new DefaultShell(IDELabel.Shell.PROPERTIES_FOR + " " + projectName);
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellWithTextIsAvailable(IDELabel.Shell.PROPERTIES_FOR + " " + projectName), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), timeout);
	}
}
