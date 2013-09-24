package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.switchyard.reddeer.widget.Link;


public class DroolsServiceWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "New File";
	private String interfaceName;
	private String fileName;

	public DroolsServiceWizard() {
		super("SwitchYard", "SwitchYard Drools Component");
	}

	public DroolsServiceWizard activate() {
		Bot.get().shell(DIALOG_TITLE).activate();
		return this;
	}

	public DroolsServiceWizard setInterface(String name) {
		this.interfaceName = name;
		return this;
	}
	
	public DroolsServiceWizard setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	@Override
	public void finish() {
		activate();
		if (fileName != null){
			new LabeledText("File name:").setText(fileName);
		}
		if (interfaceName != null) {
			new Link("Interface:").click();
			new JavaInterfaceWizard().activate().setName(interfaceName).finish();
			activate();
		}
		super.finish();
	}
}
