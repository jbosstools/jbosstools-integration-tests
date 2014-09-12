package org.jboss.tools.ws.ui.bot.test.uiutils;

import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;

/**
 * 
 * @author Radoslav Rabara
 *
 */
public class JavaBuildPathPropertiesPage {

	/**
	 * Selects this page in {@link PropertiesDialog} 
	 */
	public void select() {
		for(TreeItem ti : new DefaultTree().getItems()) {
			if(ti.getCell(0).equals("Java Build Path")) {
				ti.doubleClick();
				return;
			}
		}
		throw new IllegalArgumentException("page was not found");
	}

	public void activateLibrariesTab() {
		new DefaultTabItem("Libraries").activate();
	}

	public void selectLibrary(Matcher<String> matcher) {
		for(TreeItem ti : new DefaultTree(1).getItems()) {
			if(matcher.matches(ti.getText())) {
				ti.select();
				return;
			}
		}
		throw new IllegalArgumentException("Item matching matcher " + matcher + " was not found");
	}
}
