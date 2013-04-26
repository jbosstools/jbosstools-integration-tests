package org.jboss.tools.bpel.reddeer.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author apodhrad
 *
 */
public class QuickPickTree {
	
	public static final String LABEL_QUICK_PICK = "Quick Pick:";

	SWTBotTree tree;

	public QuickPickTree() {
		tree = Bot.get().treeWithLabel(LABEL_QUICK_PICK);
	}

	public void pick(String operation) {
		findItems(tree.getAllItems(), operation).get(0).select();
	}

	public void pick(String[] path) {
		tree.expandNode(path).select();
	}

	private List<SWTBotTreeItem> findItems(SWTBotTreeItem[] treeItem, String label) {
		List<SWTBotTreeItem> treeItems = new ArrayList<SWTBotTreeItem>();
		for (int i = 0; i < treeItem.length; i++) {
			treeItems.addAll(findItems(treeItem[i], label));
		}
		return treeItems;
	}

	private List<SWTBotTreeItem> findItems(SWTBotTreeItem treeItem, String label) {
		List<SWTBotTreeItem> treeItems = new ArrayList<SWTBotTreeItem>();
		if (treeItem.getText().equals(label)) {
			treeItems.add(treeItem);
		}
		SWTBotTreeItem[] ti = treeItem.getItems();
		for (int i = 0; i < ti.length; i++) {
			treeItems.addAll(findItems(ti[i], label));
		}
		return treeItems;
	}
}
