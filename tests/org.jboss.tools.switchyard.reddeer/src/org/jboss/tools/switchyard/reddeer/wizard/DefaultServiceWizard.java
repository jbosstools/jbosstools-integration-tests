package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;

/**
 * 
 * @author apodhrad
 *
 */
public class DefaultServiceWizard extends ServiceWizard<DefaultServiceWizard> {

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}

}
