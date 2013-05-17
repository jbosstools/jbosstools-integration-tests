package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

public class DynamicWebProjectWizard extends NewWizardDialog{

	public static final String CATEGORY="Web";
	public static final String NAME="Dynamic Web Project";
	
	public DynamicWebProjectWizard(){
		super(CATEGORY,NAME);
		addWizardPage(new DynamicWebProjectFirstPage(), 1);
		addWizardPage(new DynamicWebProjectThirdPage(), 3);
	}

}
