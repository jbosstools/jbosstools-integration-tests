package org.jboss.tools.eclipsecs.ui.test.view;

import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Checkstyle violations view
 * @author Jiri Peterka
 *
 */
public class MarkerStatsView extends WorkbenchView {

	/**
	 * Initialization and lookup for Checkstyle violations view
	 */
	public MarkerStatsView() {
		super("Checkstyle violations");
	}
	
	
	/**
	 * Checkstyle violations item count
	 * Return Checkstyle violations item count
	 */
	public int getItemCount() {
		DefaultTable t = new DefaultTable(this);
		return t.getItems().size();		
	}

}
