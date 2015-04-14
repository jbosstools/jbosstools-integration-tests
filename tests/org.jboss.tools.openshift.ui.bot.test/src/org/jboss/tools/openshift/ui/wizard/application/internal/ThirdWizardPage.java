package org.jboss.tools.openshift.ui.wizard.application.internal;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.ui.condition.ButtonWithTextIsAvailable;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;

/**
 * 
 * Third wizard page of a New application wizard.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ThirdWizardPage {

	public ThirdWizardPage() {
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);

		new WaitUntil(new ButtonWithTextIsAvailable(OpenShiftLabel.Button.BROWSE), 
				TimePeriod.LONG);
	}
	
	/**
	 * Fill in application details.
	 *  
	 * @param createAdapter set true if should be created, false otherwise
	 * @param project project name of an existing project or null if you want to create application from scratch
	 * @param disableMvnBuild set true if maven build should be disabled, false otherwise
	 */
	public void configureProjectAndServerAdapter(boolean createAdapter, String project, boolean disableMvnBuild) {
	    if (new CheckBox(1).isChecked() != createAdapter) {
	    	new CheckBox(1).click();
	    }
		
		if (project != null) {
			if (new CheckBox(0).isChecked()) {
				new CheckBox(0).click();
			} 
			new LabeledText("Use existing project:").setText(project);
		} else {
			if (!new CheckBox(0).isChecked()) {
				new CheckBox(0).click();
			}
		}
		
		if (disableMvnBuild) {
			new CheckBox(2).click();
		}
	}
	
}
