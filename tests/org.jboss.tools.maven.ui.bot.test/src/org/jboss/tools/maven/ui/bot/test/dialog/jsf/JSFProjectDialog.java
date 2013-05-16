package org.jboss.tools.maven.ui.bot.test.dialog.jsf;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

public class JSFProjectDialog extends NewWizardDialog{
	
	public static final String CATEGORY1="JBoss Tools Web";
	public static final String CATEGORY2="JSF";
	public static final String NAME="JSF Project";
	
	public JSFProjectDialog(){
		super(CATEGORY1,CATEGORY2,NAME);
		addWizardPage(new JSFProjectFirstPage(), 1);
		addWizardPage(new JSFProjectSecondPage(), 2);
	}	
	

}
