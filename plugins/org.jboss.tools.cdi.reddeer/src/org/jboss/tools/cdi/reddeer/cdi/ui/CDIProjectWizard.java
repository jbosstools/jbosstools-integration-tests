package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.facet.CDIInstallWizardPage;

public class CDIProjectWizard extends NewWizardDialog{

	public static final String CATEGORY="CDI (Context and Dependency Injection)";
	public static final String NAME="CDI Web Project";
	
	public CDIProjectWizard(){
		super(CATEGORY,NAME);
		addWizardPage(new WebProjectFirstPage(), 0);
		addWizardPage(new CDIInstallWizardPage(), 3);
	}

}
