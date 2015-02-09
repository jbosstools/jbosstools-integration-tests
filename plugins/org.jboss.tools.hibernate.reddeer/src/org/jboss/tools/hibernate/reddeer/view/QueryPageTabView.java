package org.jboss.tools.hibernate.reddeer.view;

import java.util.List;

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;


/**
 * Hibernate Query Result View RedDeer implementation
 * @author Jiri Peterka
 *
 */
public class QueryPageTabView extends WorkbenchView {

	/**
	 * Initialization and lookup for Hibernate Query Result View
	 */
	public QueryPageTabView() {
		super("Hibernate Query Result");
	}
	
	
	/**
	 * Return Hibernate Query Result table items
	 */
	public List<TableItem> getResultItems() {
		return new DefaultTable().getItems();
	}

}
