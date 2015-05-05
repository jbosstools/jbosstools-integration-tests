package org.jboss.tools.arquillian.ui.bot.reddeer.junit;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Page for JUnit test case definition
 * 
 * @author Lucia Jelinkova
 *
 */
public class JUnitTestCaseWizardPage extends WizardPage {

	public void setSourceFolder(String text){
		new LabeledText("Source folder:").setText(text);
	}
	
	public void setPackage(String text){
		new LabeledText("Package:").setText(text);
	}
	
	public void setName(String text){
		new LabeledText("Name:").setText(text);
	}
	
	public void setSuperclass(String text){
		new LabeledText("Superclass:").setText(text);
	}
	
	public void setClassUnderTest(String text){
		new LabeledText("Class under test:").setText(text);
	}
}
