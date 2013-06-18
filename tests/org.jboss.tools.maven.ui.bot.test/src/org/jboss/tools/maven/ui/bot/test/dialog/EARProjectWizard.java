package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

public class EARProjectWizard extends NewWizardDialog{

	public static final String CATEGORY="Java EE";
	public static final String NAME="Enterprise Application Project";
	
	public EARProjectWizard(){
		super(CATEGORY,NAME);
		addWizardPage(new EARProjectFirstPage(),1);
		addWizardPage(new EARProjectSecondPage(),2);
	}
}
