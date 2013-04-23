package org.eclipse.bpmn2.ui.swt.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.swt.api.TreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class TreeItemWithText extends BaseMatcher<TreeItem> {

	private String text;

	public TreeItemWithText(String text) {
		this.text = text;
	}

	public boolean matches(Object item) {
		if (item instanceof TreeItem) {
			return ((TreeItem) item).getText().equals(text);
		}
		return false;
	}

	public void describeTo(Description description) {
		description.appendText("TreeItem with text '" + text + "'");

	}

}
