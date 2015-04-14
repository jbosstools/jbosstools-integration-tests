package org.jboss.tools.openshift.ui.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.swt.api.Table;

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
