package org.eclipse.bpmn2.ui.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BPMN2GenericModelWizard extends NewWizardDialog {

	/**
	 * 
	 */
	public enum ModelType {
		
		PROCESS(0),
		COLLABORATION(1),
		CHOREOGRAPHY(2);
		
		private int buttonIndex;
		
		ModelType(int buttonIndex) {
			this.buttonIndex = buttonIndex;
		}
		
		public int getButtonIndex() {
			return buttonIndex;
		}
	}
	
	/**
	 * Creates a new instance of NewGenericBpmn2ModelWizard. 
	 */
	public BPMN2GenericModelWizard() {
		super("BPMN2", "Generic BPMN 2.0 Diagram");
	}
	
	@Override
	public WizardPage getFirstPage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param fileName
	 * @param path
	 * @param targetNamespace
	 * @param type
	 */
	public void execute(String fileName, String[] path, String targetNamespace, ModelType type) {
		open();
		new PushButton(type.getButtonIndex()).click();
		next();
		new LabeledText("Location:").setText(ProjectPath.valueOf(path));
		new LabeledText("File name:").setText(fileName);
		new LabeledText("Target Namespace:").setText(targetNamespace);
		finish();
	}
}
