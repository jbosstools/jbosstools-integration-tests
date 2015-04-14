package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * Wizard for JPA Entities generation
 * @author Jiri Peterka
 *
 */
public class GenerateEntitiesWizard {

	/**
	 * Initializes Generate Entities Wizard 
	 */
	public GenerateEntitiesWizard() {
	}
	
	/**
	 * Opens Wizard for JPA entities generation
	 */
	public void open() {
		new ContextMenu("JPA Tools","Generate Entities from Tables...").select();
	}
	
	/**
	 * Clicks finish button
	 */
	public void finish() {	
		new FinishButton().click();
		new WaitWhile(new JobIsRunning());
	}
}
