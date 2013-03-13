package org.jboss.tools.bpel.ui.bot.ext.view;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.ViewTree;
import org.jboss.tools.bpel.ui.bot.ext.matcher.TreeItemWithText;

/**
 * 
 * @author apodhrad
 *
 */
public class BPELOutlineView extends OutlineView {

	public void select(String label) {
		TreeItem itemToSelect = null;

		BPELOutlineView outline = new BPELOutlineView();
		outline.open();
		itemToSelect = outline.findItem(label);

		if (itemToSelect == null) {
			throw new RuntimeException("Cannot find activity with label '" + label + "'");
		}
		itemToSelect.select();
	}

	public TreeItem findItem(String label) {
		return findItem(label, 0);
	}

	public TreeItem findItem(String label, int index) {
		List<TreeItem> foundItems = findItems(new TreeItemWithText(label));
		if (foundItems.isEmpty()) {
			throw new RuntimeException("Cannot find TreeItem with text '" + label + "' at index '"
					+ index + "'");
		}
		return foundItems.get(index);
	}

	public List<TreeItem> findItems(Matcher<TreeItem> matcher) {
		List<TreeItem> items = new ArrayList<TreeItem>();
		Tree tree = new ViewTree();
		List<TreeItem> allItems = tree.getAllItems();
		for (TreeItem item : allItems) {
			if (matcher.matches(item)) {
				items.add(item);
			}
		}
		return items;
	}

}
