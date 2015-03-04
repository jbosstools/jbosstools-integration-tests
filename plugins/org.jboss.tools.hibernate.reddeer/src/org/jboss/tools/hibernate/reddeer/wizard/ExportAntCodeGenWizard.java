package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.ExportWizardDialog;


/**
 * RedDeer dialog for Ant Hibernate Code Generation configuration export
 * @author Jiri Peterka
 *
 */
public class ExportAntCodeGenWizard extends ExportWizardDialog {

	public static final String CATEGORY = "Hibernate";
	
	public static final String NAME = "Ant Code Generation";
	
	/**
	 * Initialize Export Ang Code Generation wizard
	 */
	public ExportAntCodeGenWizard() {
		super(CATEGORY, NAME);
	}

}