package org.jboss.tools.openshift.ui.bot.test.customizedexplorer;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.AbstractShell;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.lookup.ShellLookup;
import org.jboss.reddeer.swt.matcher.AndMatcher;
import org.jboss.reddeer.swt.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.reference.ReferencedComposite;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Customized project allow to work with git projects.
 * Delete some customized project do not fail in case of failure, 
 * just flush message on error log - it is caused by upstream bug 
 * and this is workaround for it (tests pass although project has not
 * been deleted from workspace) 
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CustomizedProject {

	private Logger logger = new Logger(CustomizedProject.class);
	
	private TreeItem treeItem;
	private String name;
	
	public CustomizedProject(TreeItem treeItem) {
		this.treeItem = treeItem;
		String text = treeItem.getText();
		if (text.charAt(0) == '>') {
			name = text.split(" ")[1];
		} else {
			name = text.split(" ")[0];
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void select() {
		treeItem.select();
		logger.info("Selected project " + name);
	}
	
	/**
	 * There is included workaround for menu items, bcs. sometimes they do not expand
	 * @param path to a file
	 */
	public void openFile(String... paths) {
		expand(treeItem);
		
		// Go through hierarchy
		TreeItem tmpItem = treeItem; 
		for (int i = 0; i < paths.length - 1; i++) {
			String path = paths[i]; 
			for (TreeItem item: tmpItem.getItems()) {
				if (parseItemName(item).equals(path)) {
					tmpItem = item;
					expand(item);
					break;
				}
			}
		}
		
		// Open file
		for (TreeItem item: tmpItem.getItems()) {
			if (parseItemName(item).equals(paths[paths.length - 1])) {
				item.select();
				logger.info("Opening file " + item.getText());
				item.doubleClick();
			}
		}
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}
	
	/**
	 * Go through hierarchy of project to return project item specified by path 
	 * @param path path to item including the desired item
	 * @return tree item if exists, null otherwise
	 */
	public TreeItem getCustomizedProjectItem(String... path) {
		expand(treeItem);
		
		TreeItem tmpItem = treeItem; 
		for (int i = 0; i < path.length - 1; i++) {
			String partialPath = path[i]; 
			for (TreeItem item: tmpItem.getItems()) {
				if (parseItemName(item).equals(partialPath)) {
					tmpItem = item;
					expand(item);
					break;
				}
			}
		}
		
		for (TreeItem item: tmpItem.getItems()) {
			if (parseItemName(item).equals(path[path.length - 1])) {
				return item;
			}
		}
		
		return null;
	}
	
	/**
	 * Find out whether specific item exists in the given customized project
	 * @param path to the desired item
	 * @return true if exists, false otherwise
	 */
	public boolean customizedProjectItemExists(String... path) {
		if (getCustomizedProjectItem(path) != null) {
			return true;
		}
		return false;		
	}
	
	/**
	 * Delete project
	 * @return true in case of success, false otherwise
	 */
	public boolean delete() {
		treeItem.select();
		
		new ContextMenu("Refresh").select();
        new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
        treeItem.select();
        
		try {
			new ContextMenu("Delete").select();
			
			new DefaultShell("Delete Resources").setFocus();;
			new CheckBox().toggle(true);
			new PushButton("OK").click();
			
			try {
				new WaitWhile(new ShellWithTextIsActive("Delete Resources"),TimePeriod.NORMAL);
				new DeleteShellWithButtonContinue().setFocus();
				new PushButton("Continue").click();
			} catch(Exception ex) { 
			}
			
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			
			return true;
		} catch (SWTLayerException ex) {
			logger.error("Not possible to delete project " + name);
			return false;
		}
	}
	
	// Bcs. of git project items
	private String parseItemName(TreeItem item) {
		if (item.getText().charAt(0) == '>') {
			return item.getText().split(" ")[1];
		} else {
			return item.getText();
		}
	}
	
	// This is a workaround for not-expanding items
	private void expand(TreeItem item) {
		item.select();
		item.expand();
		item.collapse();
		item.expand();
		logger.info("Expanded tree item " + item.getText());
	}
	
	private class DeleteShellWithButtonContinue extends AbstractShell {

		public DeleteShellWithButtonContinue() {
			super(lookForShellWithButton("Delete Resources", "Continue"));
			setFocus();
		}
	}

	private static Shell lookForShellWithButton(final String title,
			final String buttonLabel) {
		Matcher<String> titleMatcher = new WithTextMatcher(title);
		Matcher<String> buttonMatcher = new BaseMatcher<String>() {
			@Override
			public boolean matches(Object obj) {
				if (obj instanceof Control) {
					final Control control = (Control) obj;
					ReferencedComposite ref = new ReferencedComposite() {
						@Override
						public Control getControl() {
							return control;
						}
					};
					try {
						new PushButton(ref, buttonLabel);
						return true;
					} catch (SWTLayerException e) {
						// ok, this control doesn't contain the button
					}
				}
				return false;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("containing button '" + buttonLabel + "'");
			}
		};
		@SuppressWarnings("unchecked")
		Matcher<String> matcher = new AndMatcher(titleMatcher, buttonMatcher);
		return ShellLookup.getInstance().getShell(matcher);
	}
}
