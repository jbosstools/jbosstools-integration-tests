package org.jboss.tools.bpel.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * 
 * @author apodhrad
 * 
 */
public class ImportProjectWizard extends ImportWizardDialog {

	public static final String ARCHIVE_LABEL = "Select archive file:";

	private String location;

	public ImportProjectWizard(String location) {
		super("General", "Existing Projects into Workspace");
		this.location = location;
	}

	public void execute() {
		open();

		if (location.toLowerCase().endsWith(".zip")) {
			new RadioButton(ARCHIVE_LABEL).click();
			new DefaultCombo(1).setText(location);
		} else {
			new DefaultCombo(0).setText(location);
		}

		new PushButton("Refresh").click();
		finish();
	}

}
