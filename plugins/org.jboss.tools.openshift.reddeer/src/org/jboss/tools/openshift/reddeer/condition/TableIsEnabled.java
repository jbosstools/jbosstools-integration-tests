package org.jboss.tools.openshift.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.api.Table;

/**
 * Wait condition waiting till specified table is active.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class TableIsEnabled extends AbstractWaitCondition {

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
