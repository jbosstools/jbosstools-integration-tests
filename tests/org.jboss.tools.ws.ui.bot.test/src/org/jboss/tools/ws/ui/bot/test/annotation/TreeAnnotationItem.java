package org.jboss.tools.ws.ui.bot.test.annotation;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

/**
 * Matches row with its SWTBotTreeItem
 * 
 * Primary motivation of using this class is to provide method isAnnotationPresence
 * in AnnotationPropertiesView. We can't just call treeItem.isCheched(),
 * because the Annotation View is using fake checkboxes - image instead of checkbox.
 * 
 * @author rrabara
 * @version 20130918
 */
public class TreeAnnotationItem {
	
	private SWTBotTreeItem treeItem;
	
	private int row;
	
	public TreeAnnotationItem(SWTBotTreeItem item, int row) {
		setRow(row);
		setTreeItem(item);
	}

	public SWTBotTreeItem getTreeItem() {
		return treeItem;
	}
	
	public String getText() {
		return treeItem.getText();
	}
	
	public void setTreeItem(SWTBotTreeItem treeItem) {
		if(treeItem == null)
			throw new NullPointerException("treeItem");
		this.treeItem = treeItem;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		if(row<0)
			throw new IllegalArgumentException("row can't be below zero");
		this.row = row;
	}
}
