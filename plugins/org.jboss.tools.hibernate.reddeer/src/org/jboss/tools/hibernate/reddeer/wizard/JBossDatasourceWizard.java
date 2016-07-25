package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.FinishButton;


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
	
	/**
	 * Click finish
	 */
	public void finish() {
		new FinishButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("New JBoss Datasource"));
	}

}
