package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.tools.cdi.reddeer.CDIConstants;

public class CDIProjectWizard extends NewWizardDialog{

	public static final String NAME="CDI Web Project";
	
	public CDIProjectWizard(){
		super(CDIConstants.CDI_GROUP,NAME);
	}

}
