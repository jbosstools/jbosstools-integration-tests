package org.jboss.tools.runtime.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class TaskWizardLoginPage extends WizardPage{
	
	public void setUsername(String username){
		new LabeledCombo("Username: ").setSelection(username);
	}
	
	public String getDomain(){
		return new LabeledCombo("Domain: ").getText();
	}
	
	public void addCredentials(String username, String password){
		new PushButton("Add...").click();
		new DefaultShell("Add a Credential");
		new LabeledText("Username: ").setText(username);
		new LabeledText("Password: ").setText(password);
		new OkButton().click();
		
		//Do not store this to secure storage
		new DefaultShell("Secure Storage Password");
		new CancelButton().click();
	}

	public boolean containsUsername(String username){
		return new LabeledCombo("Username: ").getItems().contains(username);
	}
	
}
