package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.switchyard.reddeer.widget.Link;

/**
 * Wizard for creating a bean service.
 * 
 * @author apodhrad
 * 
 */
public class BeanServiceWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "New Bean Service";

	private String interfaceName;

	public BeanServiceWizard() {
		super("SwitchYard", "SwitchYard Bean Component");
	}

	public BeanServiceWizard activate() {
		Bot.get().shell(DIALOG_TITLE).activate();
		return this;
	}

	public BeanServiceWizard setInterface(String name) {
		this.interfaceName = name;
		return this;
	}

	@Override
	public void finish() {
		activate();
		if (interfaceName != null) {
			new Link("Interface:").click();
			new JavaInterfaceWizard().activate().setName(interfaceName).finish();
			activate();
		}
		super.finish();
	}

}
