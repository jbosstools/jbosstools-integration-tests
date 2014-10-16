package org.jboss.tools.runtime.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

public class TaskWizardLoginPage extends WizardPage{
	
	public void setUsername(String username){
		new LabeledText("Username: ").setText(username);
	}
	
	public void setPassword(String password){
		new LabeledText("Password: ").setText(password);
	}

}
