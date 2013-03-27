package org.jboss.tools.bpmn2.ui.bot.ext.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.ui.bot.test.suite.PathInProject;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class NewJBpmProcessWizard extends NewWizardDialog {

	/**
	 * Creates a new instance of NewJBpmProcessWizard.
	 */
	public NewJBpmProcessWizard() {
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
		new LabeledText("Container:").setText(PathInProject.buildPath(path));
		new LabeledText("File name:").setText(fileName);
		new LabeledText("Process name:").setText(processName);
		new LabeledText("Process ID:").setText(processId);
		new LabeledText("Package:").setText(packageName);
		finish();
	}
	
}
