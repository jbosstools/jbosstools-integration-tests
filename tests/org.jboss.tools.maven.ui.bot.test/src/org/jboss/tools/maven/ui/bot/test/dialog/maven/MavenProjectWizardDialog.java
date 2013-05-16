package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

public class MavenProjectWizardDialog extends NewWizardDialog{
	
	public static final String CATEGORY="Maven";
	public static final String NAME="Maven Project";
	
	public MavenProjectWizardDialog(){
		super(CATEGORY,NAME);
		addWizardPage(new MavenProjectWizardSecondPage(), 2);
		addWizardPage(new MavenProjectWizardThirdPage(), 3);
	}

}
