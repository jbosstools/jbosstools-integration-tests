package org.jboss.tools.maven.ui.bot.test.utils;

import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.common.condition.AbstractWaitCondition;

public class TreeCanBeExpanded extends AbstractWaitCondition{

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
