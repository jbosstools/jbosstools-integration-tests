package org.jboss.tools.bpmn2.itests.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JavaProjectWizard extends NewWizardDialog {

	/**
	 * 
	 */
	public JavaProjectWizard() {
		super("Java", "Java Project");
	}
	
	@Override
	public WizardPage getFirstPage() {
		return null;
	}	
	
	/**
	 * 
	 * @param projectName
	 */
	public void execute(String projectName) {
		open();
		new LabeledText("Project name:").setText(projectName);
		finish();
	}
	
}
