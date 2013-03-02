package org.jboss.tools.bpel.ui.bot.ext.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 * 
 */
public class NewDescriptorWizard extends NewWizardDialog {

	public static final String LABEL_PROJECT = "BPEL Project:";

	private String projectName;

	public NewDescriptorWizard(String projectName) {
		super("BPEL 2.0", "BPEL Deployment Descriptor");
		this.projectName = projectName;
	}

	@Override
	public WizardPage getFirstPage() {
		throw new UnsupportedOperationException();
	}

	public void execute() {
		open();

		new LabeledText(LABEL_PROJECT).setText(projectName + "/bpelContent");

		finish();
	}
}
