package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;

/**
 * Wizard for creating a new service.
 * 
 * @author apodhrad
 * 
 */
public class NewServiceWizard extends ServiceWizard<NewServiceWizard> {

	public NewServiceWizard() {
		super("New Service");
	}

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}
}
