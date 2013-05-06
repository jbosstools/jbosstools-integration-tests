package org.jboss.tools.bpmn2.itests.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPMProcessLegacyWizard extends NewWizardDialog {

	/**
	 * Creates a new instance of NewProcessWizard. 
	 */
	public JBPMProcessLegacyWizard() {
		super("jBPM", "BPMN2 Process");
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
		execute(new String[0], processName);
	}
	
	/**
	 * Create a new process definition.
	 * 
	 * @param location    path where the file is supposed to be stored (including project name)
	 * @param processName name of the file
	 */
	public void execute(String[] location, String processName) {
		open();
		new LabeledText("Enter or select the parent folder:").setText(ProjectPath.valueOf(location));
		new LabeledText("File name:").setText(processName);
		finish();			
	}
	
}
