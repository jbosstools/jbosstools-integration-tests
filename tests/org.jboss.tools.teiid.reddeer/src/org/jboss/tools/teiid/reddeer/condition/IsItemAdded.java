package org.jboss.tools.teiid.reddeer.condition;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;

/**
 * Condition that specifies if a given item was added
 * 
 * @author apodhrad
 * 
 */
public class IsItemAdded implements WaitCondition {

	private String item;
	private String tab;

	public IsItemAdded(String item, String tab) {
		this.item = item;
		this.tab = tab;
	}

	@Override
	public boolean test() {
		List<String> addedItems = getAddedItems(tab);
		return addedItems.contains(item);
	}

	@Override
	public String description() {
		return tab + " element '" + item + "' wasn't added!";
	}

	private List<String> getAddedItems(String tab) {
		new DefaultTabItem(tab).activate();
		List<String> result = new ArrayList<String>();
		if (tab.equals("Request")) {
			List<TreeItem> items = new DefaultTree(2).getItems();
			for (TreeItem item : items) {
				result.add(item.getText());
			}
		}
		if (tab.equals("Response")) {
			Table table = new DefaultTable();
			for (int i = 0; i < table.rowCount(); i++) {
				result.add(table.cell(i, 0));
			}
		}
		return result;
	}

}
