package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class ServerWizard extends NewWizardDialog {

	private String[] type;
	private String name;

	public ServerWizard() {
		super("Server", "Server");
	}

	public void setType(String[] type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void execute() {
		open();

		new DefaultTreeItem(0, type).select();//eclipse kepler (0), eclipse juno (1)
		new LabeledText("Server name:").setText(name);

		finish();
	}
}
