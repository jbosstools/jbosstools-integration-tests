package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class ReferenceWizard extends ServiceWizard {

	public ReferenceWizard() {
		super();
	}

	public ReferenceWizard open() {
		new SwitchYardEditor().addComponent("Reference");
		return this;
	}

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}

}
