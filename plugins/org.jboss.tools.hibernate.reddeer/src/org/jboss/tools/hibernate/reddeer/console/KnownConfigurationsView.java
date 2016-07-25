package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/** 
 * Hibernate configuration view implementation
 * @author jpeterka
 *
 */
public class KnownConfigurationsView extends WorkbenchView
{
	
	/**
	 * View implementation
	 */
	public KnownConfigurationsView() {
		super("Hibernate Configurations");
	}

	/**
	 * Add configuration
	 */
	public void triggerAddConfigurationDialog() {
		open();
		new ContextMenu("Add Configuration...").select();
		new WaitUntil(new ShellWithTextIsAvailable("Edit Configuration") );		
	}

	/**
	 * Selects console
	 * @param name given console name
	 */
	public void selectConsole(String name) {
		open();
		new DefaultTreeItem(name).select();
	}

	/**
	 * Open console configuration
	 * @param name given console name
	 * @return shell of the console
	 */
	public EditConfigurationShell openConsoleConfiguration(String name) {
		selectConsole(name);
		String title = "Edit Configuration";
		new ContextMenu(title).select();
		new WaitUntil(new ShellWithTextIsAvailable(title));
		return new EditConfigurationShell();
	}

	/**
	 * Select tree under hibernate console configuration tree
	 * @param path given path starting with console name
	 */
	public void selectNode(String... path) {		
		open();
		
		// TODO commented because unability RedDeer to expand this tree
		// new DefaultTreeItem(path).select();
	}
	
	
	/**
	 * Deletes hibernate console configuration
	 * @param console hibernate console configuration name
	 */
	public void deleteConsoleConfiguration(String console) {
		new DefaultTreeItem(console).select();
		new ContextMenu("Delete Configuration").select();
		String title = "Delete console configuration";
		new WaitUntil(new ShellWithTextIsAvailable(title));
		new DefaultShell(title);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(title));
	}
}
