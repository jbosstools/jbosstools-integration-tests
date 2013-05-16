package org.jboss.tools.maven.ui.bot.test.dialog.seam;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

public class SeamProjectDialog extends NewWizardDialog{
	
	public static final String CATEGORY="Seam";
	public static final String NAME="Seam Web Project";
	
	public SeamProjectDialog(){
		super(CATEGORY,NAME);
		addWizardPage(new SeamProjectFirstPage(), 1);
	}
	
	

}
