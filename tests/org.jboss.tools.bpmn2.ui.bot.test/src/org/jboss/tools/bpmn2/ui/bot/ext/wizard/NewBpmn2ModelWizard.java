package org.jboss.tools.bpmn2.ui.bot.ext.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class NewBpmn2ModelWizard extends NewWizardDialog {

	org.jboss.tools.bpmn2.ui.bot.ext.wizard.legacy.NewJBpmProcessWizard wizard = 
			new org.jboss.tools.bpmn2.ui.bot.ext.wizard.legacy.NewJBpmProcessWizard();
	
	/**
	 * Creates a new instance of NewBpmn2ModelWizard. 
	 */
	public NewBpmn2ModelWizard() {
		super("BPMN2", "BPMN2 Model");
	}
	
	@Override
	public WizardPage getFirstPage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Create a new process definition in the root of the first project.
	 * 
	 * @param processName name of the file.
	 */
	public void execute(String processName) {
		wizard.execute(processName);
	}
	
	/**
	 * Create a new process definition.
	 * 
	 * @param processName name of the file
	 * @param path        path where the file is supposed to be stored (including project name)
	 */
	public void execute(String processName, String[] path) {
		wizard.execute(processName, path);			
	}

}
