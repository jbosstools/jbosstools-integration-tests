package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.TicketMonsterBaseTest;

public abstract class AbstractPart2Test extends TicketMonsterBaseTest{
	
	public void removeAllRuntimes(){
		RuntimePreferencePage rp = new RuntimePreferencePage();
		rp.open();
		rp.removeAllRuntimes();
		rp.ok();
	}

}
