package org.jboss.tools.arquillian.ui.bot.reddeer.junit;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * Wizard for creating Arquillian JUnit test cases
 * 
 * @author Lucia Jelinkova
 *
 */
public class ArquillianJUnitTestCaseWizard extends NewWizardDialog {

	public ArquillianJUnitTestCaseWizard() {
		super("Arquillian", "Arquillian JUnit Test Case");
	}
}
