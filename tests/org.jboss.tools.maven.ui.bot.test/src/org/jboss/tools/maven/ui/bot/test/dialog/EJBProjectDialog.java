package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

public class EJBProjectDialog extends NewWizardDialog{

	
	public static final String CATEGORY="EJB";
	public static final String NAME="EJB Project";
	
	public EJBProjectDialog(){
		super(CATEGORY,NAME);
		addWizardPage(new EJBProjectFirstPage(), 0);
	}
}
