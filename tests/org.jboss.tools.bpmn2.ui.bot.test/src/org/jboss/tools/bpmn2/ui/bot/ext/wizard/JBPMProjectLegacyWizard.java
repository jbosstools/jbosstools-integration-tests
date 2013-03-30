package org.jboss.tools.bpmn2.ui.bot.ext.wizard;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.RadioButton;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPMProjectLegacyWizard extends NewWizardDialog {

	public enum ProcessType {
		
		SIMPLE(0),
		ADVANCED(1),
		NONE(2);
		
		private int buttonIndex;
		
		ProcessType(int buttonIndex) {
			this.buttonIndex = buttonIndex;
		}
		
		public int getButtonIndex() {
			return buttonIndex;
		}
	}
	
	/**
	 * 
	 */
	public JBPMProjectLegacyWizard() {
		super("jBPM", "jBPM project");
	}
	
	@Override
	public WizardPage getFirstPage() {
		// TODO Auto-generated method stub
		return null;
	}	
	
	/**
	 * 
	 * @param projectName
	 */
	public void execute(String projectName) {
		execute(projectName, ProcessType.NONE, false, false);
	}
	
	/**
	 * 
	 * @param projectName
	 * @param processType
	 */
	public void execute(String projectName, ProcessType processType, boolean includeMainClass, boolean includeTestClass) {
		open();
		new LabeledText("Project name:").setText(projectName);
		next();
		new RadioButton(processType.getButtonIndex()).click();
		if (!includeMainClass) new CheckBox(1).click();
		if (!includeTestClass) new CheckBox(0).click();
		finish();
		assertTrue("Project '" + projectName + "' was not created", new PackageExplorer().containsProject(projectName));
	}
	
}
