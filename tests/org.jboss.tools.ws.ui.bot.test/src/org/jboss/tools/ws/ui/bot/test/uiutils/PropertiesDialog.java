package org.jboss.tools.ws.ui.bot.test.uiutils;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
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
	
		// Open Project Properties
		assertThat("Project name", project.getName(), Is.is(projectName));
		assertThat("Project with name '" + projectName + "' is selected",
				project.isSelected(), Is.is(true));
	
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
		new PushButton(IDELabel.Button.OK).click();
		new WaitWhile(new ShellWithTextIsAvailable(IDELabel.Shell.PROPERTIES_FOR + " " + projectName), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning());
	}
	
	public void finish(TimePeriod timeout) {
		new PushButton(IDELabel.Button.OK).click();
		new WaitWhile(new ShellWithTextIsAvailable(IDELabel.Shell.PROPERTIES_FOR + " " + projectName), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), timeout);
	}
}
