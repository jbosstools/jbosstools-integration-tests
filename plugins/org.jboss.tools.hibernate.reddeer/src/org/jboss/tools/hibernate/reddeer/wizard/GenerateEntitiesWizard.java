package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Wizard for JPA Entities generation
 * @author Jiri Peterka
 *
 */
public class GenerateEntitiesWizard {

	
	public GenerateEntitiesWizard() {
	}
	
	public void open() {
		new ContextMenu("JPA Tools","Generate Entities from Tables...").select();
	}
	
	public void finish() {	
		new FinishButton().click();
		new WaitWhile(new JobIsRunning());
	}
}
