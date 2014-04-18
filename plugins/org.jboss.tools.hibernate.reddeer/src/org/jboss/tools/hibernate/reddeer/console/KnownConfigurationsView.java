package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

public class KnownConfigurationsView extends WorkbenchView
{
	
	public KnownConfigurationsView() {
		super("Hibernate Configurations");
	}

	public void triggerAddConfigurationDialog() {
		open();
		new ContextMenu("Add Configuration...").select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration") );		
	}

	public void selectConsole(String name) {
		open();
		new DefaultTreeItem(name).select();
	}

	public EditConfigurationShell openConsoleConfiguration(String name) {
		selectConsole(name);
		String title = "Edit Configuration";
		new ContextMenu(title).select();
		new WaitUntil(new ShellWithTextIsActive(title));
		return new EditConfigurationShell();
	}

	public void selectNode(String... path) {		
		open();
		new DefaultTreeItem(path).select();		
	}
}
