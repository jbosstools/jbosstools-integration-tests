package org.jboss.tools.switchyard.reddeer.widget;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.matcher.WithMnemonicMatcher;
import org.jboss.reddeer.swt.util.Bot;

/**
 * Extension for project item.
 * 
 * @author apodhrad
 * 
 */
public class ProjectItemExt {

	private ProjectItem projectItem;

	public ProjectItemExt(ProjectItem projectItem) {
		this.projectItem = projectItem;
	}

	@SuppressWarnings("unchecked")
	public void runAs(String menu) {
		projectItem.select();
		new ContextMenu(new WithMnemonicMatcher("Run As"), new MenuMatcher(menu)).select();
		Bot.get().sleep(60 * 1000);
	}

	public void runAsJUnitTest() {
		runAs("JUnit Test");
	}

	private class MenuMatcher extends BaseMatcher<String> {

		private String text;

		public MenuMatcher(String text) {
			super();
			this.text = text;
		}

		@Override
		public boolean matches(Object obj) {
			if (obj instanceof String) {
				String label = (String) obj;
				return label.contains(text);
			}
			return false;
		}

		@Override
		public void describeTo(Description desc) {
			desc.appendText("menu item containing '" + text + "'");
		}

	}
}
