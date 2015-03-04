package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
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
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration") );		
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
		new WaitUntil(new ShellWithTextIsActive(title));
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
}
