package org.jboss.tools.arquillian.ui.bot.reddeer.view;

import org.apache.log4j.Logger;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Represents the Build List view - to support Mylyn automated tests. 
 *  
 * @author ldimaggi
 *
 */
public class ArquilliaCruiserView extends WorkbenchView {
	
	protected final static Logger log = Logger.getLogger(ArquilliaCruiserView.class);

	public static final String TITLE = "Arquillia Cruiser";
	
	public ArquilliaCruiserView() {
		super(TITLE);
	}

	public Tree getTree(){
		open();
		return new DefaultTree();
	}
	
	/* Method to locate and select a build in the build list view  */
	public TreeItem getTreeItem (String... arquillianTreeItem ) {
		new DefaultTree();
		DefaultTreeItem theTreeItem = new DefaultTreeItem (arquillianTreeItem);
		theTreeItem.select();
		return theTreeItem;
	}
	
}
