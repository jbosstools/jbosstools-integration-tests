package org.jboss.tools.arquillian.ui.bot.reddeer.junit;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * Wizard for creating JUnit test cases
 * 
 * @author Lucia Jelinkova
 *
 */
public class JUnitTestCaseWizard extends NewWizardDialog{
	
	public JUnitTestCaseWizard() {
		super("Java", "JUnit", "JUnit Test Case");
	}
}
