package org.jboss.tools.ws.reddeer.ui.wizards;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.common.reddeer.label.IDELabel;

/**
 * Specialized parent of wizards dialogs that has associated perspective.<br/>
 * 
 * Provides specialized {@link #finish()} method that handles opening of shell
 * with text {@link IDELabel.Shell.OPEN_ASSOCIATED_PERSPECTIVE}.
 *
 * @author Radoslav Rabara
 *
 */
public abstract class NewWizardDialogWithAssociatedPerspective extends NewWizardDialog {

	/**
	 * {@inheritDoc}
	 */
	public NewWizardDialogWithAssociatedPerspective(String... path) {
		super(path);
	}

	/**
	 * Clicks on the button Finish, then if the shell open associated perspective
	 * is active, closes it permanently, and waits while any job is running.
	 */
	@Override
	public void finish() {
		String shellText = new DefaultShell().getText();

		new PushButton(IDELabel.Button.FINISH).click();

		closeOpenAssociatedPerspectiveDialog();

		new WaitWhile(new ShellWithTextIsActive(shellText), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	private void closeOpenAssociatedPerspectiveDialog() {
		WaitCondition condition = new ShellWithTextIsActive(
				IDELabel.Shell.OPEN_ASSOCIATED_PERSPECTIVE);
		new WaitUntil(condition, TimePeriod.NORMAL, false);
		if(condition.test()) {
			CheckBox checkbox = new CheckBox(IDELabel.Shell.REMEMBER_MY_DECISION);
			if(!checkbox.isChecked()) {
				checkbox.click();
			}
			new PushButton(IDELabel.Button.NO).click();
		}
	}
}
