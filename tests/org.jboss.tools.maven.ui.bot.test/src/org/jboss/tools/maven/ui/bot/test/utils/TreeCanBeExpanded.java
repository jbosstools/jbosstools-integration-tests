package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class TreeCanBeExpanded implements WaitCondition{

	private TreeItem treeItem;
	
	public TreeCanBeExpanded(TreeItem treeItem){
		this.treeItem = treeItem;
	}
	
	@Override
	public boolean test() {
		return treeItem.getItems().size() > 0;
//		return treeItem.hasChildren();
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

}
