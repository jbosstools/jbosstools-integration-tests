package org.jboss.tools.openshift.ui.condition;

import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.condition.WaitCondition;

/**
 * Wait condition waiting till specified table is active.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class TableIsEnabled implements WaitCondition {

	private Table table;
	
	public TableIsEnabled(Table table) {
		this.table = table;
	}

	@Override
	public boolean test() {
		return table.isEnabled();
	}

	@Override
	public String description() {
		return "table is enabled";
	}
	
	
}
