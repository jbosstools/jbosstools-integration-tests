package org.jboss.tools.bpel.reddeer.wizard;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class NewProjectWizard extends NewWizardDialog {

	private String name;

	public NewProjectWizard(String name) {
		super("BPEL 2.0", "BPEL Project");
		this.name = name;
	}

	@Override
	public WizardPage getFirstPage() {
		return null;
	}

	public void execute() {
		open();

		new LabeledText("Project name:").setText(name);

		finish();

		assertTrue("Project '" + name + "' wasn't created",
				new PackageExplorer().containsProject(name));
	}
}
