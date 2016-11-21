package org.jboss.tools.hibernate.reddeer.console;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Hibernate configuration view reddeer implementation
 * @author jpeterka
 *
 */
public class HibernateConfigurationView extends WorkbenchView {

	/**
	 * View constructor
	 */
	public HibernateConfigurationView() {
		super("Hibernate Configurations");
	}

	/**
	 * Return list of available HibernateConfigurations
	 * @return available configurations
	 */
	public List<HibernateConfiguration> getConfigurations() {
		Tree tree = new DefaultTree();
		List<TreeItem> allItems = tree.getAllItems();
		List<HibernateConfiguration> configurations = new ArrayList<HibernateConfiguration>();
		
		for (TreeItem i : allItems) {
			HibernateConfiguration c = new HibernateConfiguration();
			c.setName(i.getText());
			
		}
		return configurations;
	}
	
	/**
	 * Returns true when Hibernate Configuration View contains given configuration
	 * @param configuration
	 * @return true if view contains given configuration
	 */
	public boolean contains(String configuration) {	
		Tree tree = new DefaultTree();
		List<TreeItem> allItems = tree.getAllItems();
		
		return allItems.contains(configuration);
	}
	
	/**
	 * Add/Creates new hibernate configuration
	 */
	public void addConfiguration(HibernateConfiguration configuration) {
		new WorkbenchShell();
		Menu m = new ShellMenu("File","New","Other...");
		m.select();
		
		new DefaultShell("New");	
		new DefaultTreeItem("Hibernate", "Hibernate Console Configuration").select();
		new PushButton("Next >").click();
		
		Shell s = new DefaultShell("");	
		
		new LabeledText("Name:").setText(configuration.getName());
		
		new DefaultText(1).setText(configuration.getProject());
		new DefaultCombo(1).setSelection(configuration.getDatabaseConnection());
		new LabeledText("Name:").setText(configuration.getName());
		new DefaultText(1).setText(configuration.getProject());
		new DefaultText(3).setText(configuration.getConfigurationFile());
		
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsAvailable(s));
	}	
}
