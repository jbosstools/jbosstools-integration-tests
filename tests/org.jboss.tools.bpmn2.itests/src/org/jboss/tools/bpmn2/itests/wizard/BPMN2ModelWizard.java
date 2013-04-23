package org.jboss.tools.bpmn2.itests.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BPMN2ModelWizard extends NewWizardDialog {

	/**
	 * Creates a new instance of NewBpmn2ModelWizard. 
	 */
	public BPMN2ModelWizard() {
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
		execute(processName, new String[0]);
	}
	
	/**
	 * Create a new process definition.
	 * 
	 * @param processName name of the file. Must end with bpmn2
	 * @param path        path where the file is supposed to be stored (including project name)
	 */
	public void execute(String processName, String[] path) {
		open();
		new LabeledText("Enter or select the parent folder:").setText(ProjectPath.valueOf(path));
		new LabeledText("File name:").setText(processName);
		finish();
	}

}
