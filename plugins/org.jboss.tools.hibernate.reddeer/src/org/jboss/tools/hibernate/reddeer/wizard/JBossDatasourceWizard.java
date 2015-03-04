package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;


/**
 * Datasource RedDeer Wizard
 * @author Jiri Peterka
 *
 */
public class JBossDatasourceWizard extends NewWizardDialog {

	/**
	 * Initialize JBoss Datasource wizard
	 */
	public JBossDatasourceWizard() {
		super("JBoss Tools", "JBoss Datasource (-ds.xml)");
	}

}
