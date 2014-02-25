package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class NewJavaEnumWizardPage extends WizardPage{
	
	public void setName(String name){
		new LabeledText("Name:").setText(name);
	}
	
	public void setPackage(String packageName) {
		new LabeledText("Package:").setText(packageName);
	}
	
	public void setSourceFolder(String sourceFolder){
		new LabeledText("Source folder:").setText(sourceFolder);
	}
	
	public String getName(){
		return new LabeledText("Name:").getText();
	}

	public String getPackage() {
		return new LabeledText("Package:").getText();
	}
}
