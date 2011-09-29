package org.jboss.tools.bpel.ui.bot.ext.widgets;


import org.apache.log4j.Logger;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;

/**
 * 
 * @author mbaluch
 */
public class EditPartEventLogger implements EditPartListener {

	protected Logger log = Logger.getLogger(EditPartEventLogger.class);
	

	public void editFinished(EditPart editpart) {
		log.info("Changed part: " + editpart);
	}
	
	public void childRemoved(EditPart child) {
		log.info("Removed part: " + child);
	}
	
	//@Override
	public void removingChild(EditPart child, int index) {
		log.info("Removing part: " + child);
	}
	
	//@Override
	public void childAdded(EditPart child, int index) {
		log.info("Added part: " + child);
	}

	//@Override
	public void partActivated(EditPart editpart) {}

	//@Override
	public void partDeactivated(EditPart editpart) {}

	//@Override
	public void selectedStateChanged(EditPart editpart) {}

}
