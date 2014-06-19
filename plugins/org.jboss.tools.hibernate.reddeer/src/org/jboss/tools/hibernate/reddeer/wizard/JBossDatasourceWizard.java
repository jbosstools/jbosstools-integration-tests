package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;


/**
 * Datasource RedDeer Wizard
 * @author Jiri Peterka
 *
 */
public class JBossDatasourceWizard extends NewWizardDialog {

	
	public JBossDatasourceWizard() {
		super("JBoss Tools", "JBoss Datasource (-ds.xml)");
	}

	@Override
	public NewJBossDatasourceWizardPage getFirstPage() {
		return new NewJBossDatasourceWizardPage();
	}
}
