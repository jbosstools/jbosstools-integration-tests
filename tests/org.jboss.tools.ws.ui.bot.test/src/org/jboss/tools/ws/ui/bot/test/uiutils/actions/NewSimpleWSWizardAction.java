package org.jboss.tools.ws.ui.bot.test.uiutils.actions;

import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.SimpleWSWizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.Type;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.Wizard;

public class NewSimpleWSWizardAction extends NewFileWizardAction {
	
	private final Type type;

	public NewSimpleWSWizardAction(Type type) {
		super();
		this.type = type;
	}

	@Override
	public SimpleWSWizard run() {
		Wizard w = super.run();
		w.selectTemplate("Web Services", "Simple Web Service");
		w.next();
		return new SimpleWSWizard(type);
	}

}
