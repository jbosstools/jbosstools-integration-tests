package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

/**
 * Wizard for creating a new model project
 * 
 * @author apodhrad
 *
 */
public class ModelProjectWizard extends NewWizardDialog {

	public static final String CATEGORY = "Teiid Designer";
	public static final String PROJECT_TITLE = "Teiid Model Project";

	public ModelProjectWizard() {
		super(CATEGORY, PROJECT_TITLE);
	}

	@Override
	public ModelProjectPage getFirstPage() {
		return new ModelProjectPage(this);
	}

}
