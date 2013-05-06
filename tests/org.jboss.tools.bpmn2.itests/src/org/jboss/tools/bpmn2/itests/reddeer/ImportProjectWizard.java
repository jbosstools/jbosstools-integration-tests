package org.jboss.tools.bpmn2.itests.reddeer;

import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;

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
			new DefaultText(2).setText(location);
		} else {
			new DefaultText(1).setText(location);
		}

		new PushButton("Refresh").click();
		finish();
	}

}
