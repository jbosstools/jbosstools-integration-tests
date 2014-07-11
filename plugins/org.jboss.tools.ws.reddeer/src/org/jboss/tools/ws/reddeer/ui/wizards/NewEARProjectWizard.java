package org.jboss.tools.ws.reddeer.ui.wizards;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

public class NewEARProjectWizard extends NewWizardDialog {
	public NewEARProjectWizard() {
		super("Java EE", "Enterprise Application Project");
	}
	
	public void finish() {
		super.finish();
		WaitCondition condition = new ShellWithTextIsActive("Open Associated Perspective?");
		new WaitUntil(condition, TimePeriod.NORMAL, false);
		if(condition.test()) {
			CheckBox checkbox = new CheckBox("Remember my decision");
			if(!checkbox.isChecked()) {
				checkbox.click();
			}
		new PushButton("No").click();
		}
	}
}
