package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class ReferenceWizard extends ServiceWizard<ReferenceWizard> {

	public static final String DIALOG_TITLE = "New Reference";
	
	public ReferenceWizard() {
		super(DIALOG_TITLE);
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
