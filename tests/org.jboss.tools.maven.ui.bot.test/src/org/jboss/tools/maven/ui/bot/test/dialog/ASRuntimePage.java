package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ASRuntimePage extends WizardPage{

	public void setHomeDirectory(String directory){
		new LabeledText("Home Directory").setText(directory);
	}
	
	public String getName(){
		return new LabeledText("Name").getText();
	}
}
