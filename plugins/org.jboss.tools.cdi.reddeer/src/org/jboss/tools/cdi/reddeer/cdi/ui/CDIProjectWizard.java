package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.jboss.tools.cdi.reddeer.CDIConstants;

public class CDIProjectWizard extends NewMenuWizard {

	public static final String NAME = "CDI Web Project";
	public static final String SHELL_TEXT = "New CDI Web Project";

	public CDIProjectWizard() {
		super(SHELL_TEXT, CDIConstants.CDI_GROUP, NAME);
	}

}
