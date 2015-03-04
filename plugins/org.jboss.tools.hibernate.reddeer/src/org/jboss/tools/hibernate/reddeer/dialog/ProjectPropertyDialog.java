package org.jboss.tools.hibernate.reddeer.dialog;

import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.CLabelWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.handler.ShellHandler;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.lookup.ShellLookup;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

/**
 * Project properties dialog
 *
 * @author Jiri Peterka
 */
public class ProjectPropertyDialog {

	public static final String DIALOG_TITLE = "Properties for ";
	private String projectName;


	public ProjectPropertyDialog(String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * Opens project property dialog. Assumes project is selected
	 */
	public void open() {		
		new ContextMenu("Properties").select();
		new DefaultShell(DIALOG_TITLE + projectName);
	}

	/**
	 * Selects the specified workbench preference page <var>page</var>.
	 * @param page preference page to be opened
	 */
	public void select(WorkbenchPreferencePage page) {
		if (page == null) {
			throw new IllegalArgumentException("page can't be null");
		}
		select(page.getPath());
	}

	/**
	 * Selects preference page with the specified <var>path</var>.
	 * @param path path in preference shell tree to specific preference page
	 */
	public void select(String... path) {
		if (path == null) {
			throw new IllegalArgumentException("path can't be null");
		}
		if (path.length == 0) {
			throw new IllegalArgumentException("path can't be empty");
		}
		TreeItem t = new DefaultTreeItem(path);
		t.select();
		
		new WaitUntil(new CLabelWithTextIsAvailable(path[path.length-1]), TimePeriod.NORMAL, false);
	}

	/**
	 * Get name of the given preference page.
	 * 
	 * @return name of preference page
	 */
	public String getPageName() {
		DefaultCLabel cl = new DefaultCLabel();
		return cl.getText();
	}
	
	/**
	 * Presses Ok button on Preference Dialog. 
	 */
	public void ok() {
		new WaitUntil(new ShellWithTextIsAvailable(DIALOG_TITLE + projectName));
		PushButton ok = new PushButton("OK");
		new WaitUntil(new ButtonWithTextIsActive(ok));
		ok.click();
		new WaitWhile(new ShellWithTextIsAvailable(DIALOG_TITLE + projectName)); 
	}

	/**
	 * Presses Cancel button on Preference Dialog. 
	 */
	public void cancel() {
		final String parentShellText = WidgetHandler.getInstance().getText(
			ShellHandler.getInstance().getParentShell(new DefaultShell(DIALOG_TITLE).getSWTWidget()));
		CancelButton cancel = new CancelButton();
		cancel.click();
		new WaitWhile(new ShellWithTextIsAvailable(DIALOG_TITLE));
		new WaitUntil(new ShellWithTextIsActive(parentShellText));
	}
	
	/**
	 * Checks if Workbench Preference dialog is opened.
	 * @return true if the dialog is opened, false otherwise
	 */
	public boolean isOpen() {
		Shell shell = ShellLookup.getInstance().getShell(DIALOG_TITLE,TimePeriod.SHORT);
		return (shell != null);		
	}
}
