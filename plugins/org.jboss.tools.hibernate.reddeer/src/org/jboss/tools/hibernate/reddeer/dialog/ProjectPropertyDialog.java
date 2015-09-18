package org.jboss.tools.hibernate.reddeer.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.condition.CLabelWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.condition.WidgetIsFound;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.core.matcher.ClassMatcher;
import org.jboss.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.core.matcher.WithStyleMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

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
	public void select(PreferencePage page) {
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
		new WaitUntil(new ShellWithTextIsAvailable(DIALOG_TITLE + projectName), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning());
		new DefaultShell(DIALOG_TITLE + projectName);
		new WaitWhile(new JobIsRunning());
		new WaitUntil(new WidgetIsFound<Button>(new ClassMatcher(Button.class),new WithMnemonicTextMatcher("OK"),new WithStyleMatcher(SWT.PUSH)),TimePeriod.LONG);
		PushButton ok = new PushButton("OK");
		new WaitUntil(new ButtonWithTextIsEnabled(ok));
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

	/**
	 * Sets focus to a dialog
	 */
	public void setFocus() {
		new DefaultShell(DIALOG_TITLE + projectName);		
	}
}
