package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Wizard for DDL generation
 * @author Jiri Peterka
 *
 */
public class GenerateDdlWizard {

	
	public GenerateDdlWizard() {
	}
	
	public void open() {
		new ContextMenu("JPA Tools","Generate Tables from Tables...").select();
	}
	
	public void finish() {	
		new FinishButton().click();
		new WaitWhile(new JobIsRunning());
	}
}
