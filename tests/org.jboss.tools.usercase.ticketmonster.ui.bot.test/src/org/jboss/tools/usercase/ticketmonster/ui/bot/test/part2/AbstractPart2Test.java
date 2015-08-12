package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.TicketMonsterBaseTest;

public abstract class AbstractPart2Test extends TicketMonsterBaseTest{
	
	protected Project ticketMonsterProject;
	
	public void removeAllRuntimes(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		RuntimePreferencePage rp = new RuntimePreferencePage();
		preferenceDialog.select(rp);		
		rp.removeAllRuntimes();
		preferenceDialog.ok();
	}

}
