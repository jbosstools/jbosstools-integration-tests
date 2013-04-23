package org.eclipse.bpmn2.ui.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPMProcessWizard extends NewWizardDialog {

	/**
	 * Creates a new instance of NewJBpmProcessWizard.
	 */
	public JBPMProcessWizard() {
		super("BPMN2", "jBPM Process Diagram");
	}
	
	@Override
	public WizardPage getFirstPage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param fileName
	 */
	public void execute(String fileName) {
		execute(fileName, new String[0]);	
	}
	
	/**
	 * 
	 * @param fileName
	 * @param path
	 */
	public void execute(String fileName, String[] path) {
		execute(fileName, path, null, null, null);
	}

	/**
	 * 
	 * @param fileName
	 * @param path
	 * @param processName
	 * @param processID
	 * @param pkg
	 */
	public void execute(String fileName, String[] path, String processName, String processId, String packageName) {
		open();
		// if these are null use predefined values by the editor wizard.
		if (path != null && path.length > 0) new LabeledText("Container:").setText(ProjectPath.valueOf(path));
		if (fileName != null && !fileName.isEmpty()) new LabeledText("File name:").setText(fileName);
		if (processName != null && !processName.isEmpty()) new LabeledText("Process name:").setText(processName);
		if (processId != null && !processId.isEmpty()) new LabeledText("Process ID:").setText(processId);
		if (packageName != null && !packageName.isEmpty()) new LabeledText("Package:").setText(packageName);
		finish();
	}
	
}
