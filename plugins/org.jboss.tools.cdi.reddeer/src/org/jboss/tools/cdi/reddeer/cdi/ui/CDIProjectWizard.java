package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.facet.CDIInstallWizardPage;
import org.jboss.tools.cdi.reddeer.CDIConstants;

public class CDIProjectWizard extends NewWizardDialog{

	public static final String NAME="CDI Web Project";
	
	public CDIProjectWizard(){
		super(CDIConstants.CDI_GROUP,NAME);
		addWizardPage(new WebProjectFirstPage(), 0);
		addWizardPage(new CDIInstallWizardPage(), 3);
	}

}
