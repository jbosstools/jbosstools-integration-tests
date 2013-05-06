package org.jboss.tools.teiid.reddeer.view;

import org.jboss.reddeer.eclipse.jdt.ui.AbstractExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class DataSourceExplorer extends AbstractExplorer {

	public DataSourceExplorer() {
		super("Data Source Explorer");
	}

	public void openSQLScrapbook(String datasource) {
		openSQLScrapbook(datasource, false);
	}

	public void openSQLScrapbook(String datasource, boolean useRegularExpression) {
		open();

		new DefaultTreeItem("Database Connections", datasource).select();
		new ContextMenu("Open SQL Scrapbook").select();
	}
}
