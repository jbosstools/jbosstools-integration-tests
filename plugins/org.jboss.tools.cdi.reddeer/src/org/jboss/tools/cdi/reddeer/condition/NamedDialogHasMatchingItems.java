package org.jboss.tools.cdi.reddeer.condition;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.OpenCDINamedBeanDialog;

public class NamedDialogHasMatchingItems extends AbstractWaitCondition{
	
	
	private OpenCDINamedBeanDialog namedDialog;
	private int matchingItems;
	
	public NamedDialogHasMatchingItems(OpenCDINamedBeanDialog namedDialog, int matchingItems) {
		this.namedDialog = namedDialog;
		this.matchingItems = matchingItems;
	}

	@Override
	public boolean test() {
		return matchingItems == namedDialog.matchingItems().size();
	}
	
	@Override
	public String description() {
		return "CDI Named Dialog has "+matchingItems +" matching items";
	}

}
