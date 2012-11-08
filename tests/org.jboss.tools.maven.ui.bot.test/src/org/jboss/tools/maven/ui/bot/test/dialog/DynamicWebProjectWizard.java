package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;

public class DynamicWebProjectWizard extends NewWizardDialog{

	public static final String CATEGORY="Web";
	public static final String NAME="Dynamic Web Project";
	
	public DynamicWebProjectWizard(){
		super(CATEGORY,NAME);
	}
	
	@Override
	public WizardPage getFirstPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
