package org.jboss.tools.openshift.ui.bot.test.application.wizard.page;

import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;

/**
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ThirdWizardPage {

	public ThirdWizardPage() {
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
		
		// Wait until data are processed - there is no other way currently
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(
				OpenShiftLabel.Button.FINISH)), TimePeriod.LONG);
	}
	
	/**
	 * Set details of deployed project - whether use existing local project or 
	 * create new on OpenShift, and set up server adapter creation
	 *  
	 * @param createAdapter true if creation adapter is desired, false otherwise
	 * @param project name of project or null if no local project should be deployed
	 */
	public void configureProjectAndServerAdapter(boolean createAdapter, String project) {
	    if ((new CheckBox(1).isChecked() && !createAdapter) ||
	    	(!new CheckBox(1).isChecked() && createAdapter)) {
	    	
	    	new CheckBox(1).click();
	    }
		
		if (project != null) {
			new CheckBox(0).click();
			
			new LabeledText("Use existing project:").setText(project);
		}
	}
	
}
