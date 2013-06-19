package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.widget.ContextButton;
import org.jboss.tools.switchyard.reddeer.wizard.ServiceTestClassWizard;

/**
 * A service component.
 * 
 * @author apodhrad
 * 
 */
public class Service extends Component {

	public Service(String tooltip) {
		super(tooltip, 0);
	}

	public Service(String tooltip, int index) {
		super(tooltip, index);
	}

	public void promoteService() {
		click();
		new ContextButton("Promote Service").click();
	}

	public void newServiceTestClass() {
		click();
		new ContextButton("New Service Test Class").click();
		new ServiceTestClassWizard().finish();
	}

	public WizardDialog addBinding(String binding) {
		new SwitchYardEditor().activateTool(binding);
		click();
		return new WizardDialog();
	}
}
