package org.jboss.tools.openshift.ui.bot.test;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

public class Utils {

	/**
	 * Useful in case of activating new shell (sometimes new shell must not have focus / be activated)
	 * 
	 * @author mlabuda@redhat.com
	 * @param previousShells
	 * @param currentShells
	 * @return last opened shell
	 */
	public static SWTBotShell getNewShell(SWTBotShell[] previousShells, SWTBotShell[] currentShells) {
		SWTBotShell shell = null;
		int count = previousShells.length;
		int counter = 0;
		
		while (counter <= count) {
			boolean isContained = false;
			for (int i=0; i<count; i++) {
				if (currentShells[counter].equals(previousShells[i])) {
					isContained = true;
				}
			}
			if (!isContained) {
				shell = currentShells[counter];
				break;
			}
			counter++;
		}
			
		return shell;
	}
	
}
