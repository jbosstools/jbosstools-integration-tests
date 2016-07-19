package org.jboss.tools.hibernate.reddeer.editor;

import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.TreeHasChildren;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;

/**
 * Reverse engineer editor RedDeer implementation
 * @author jpeterka
 *
 */
public class RevengEditor extends DefaultEditor {

	/**
	 * Activates editor's Overview tab
	 */
	public void activateOverviewTab() {
		new DefaultCTabItem("Overview").activate();
	}

	/**
	 * Activates editor's Type Mappings tab
	 */
	public void activateTypeMappingsTab() {
		new DefaultCTabItem("Type Mappings").activate();
	}

	/**
	 * Activates editor's Type Filters tab
	 */
	public void activateTableFiltersTab() {
		new DefaultCTabItem("Table Filters").activate();
	}

	/**
	 * Activates editor's Table and Columns tab
	 */	
	public void  activateTableAndColumnsTab() {
		new DefaultCTabItem("Table  Columns").activate();
	}
	
	/**
	 * Activates editor's Design tab
	 */
	public void activateDesignTab() {
		new DefaultCTabItem("Design").activate();
	}
	
	/**
	 * Activates editor's Source tab
	 */
	public void activateSourceTab() {
		new DefaultCTabItem("Source").activate();
	}

	/**
	 * Select all tables within Add Tables & Columns tab
	 */	
	public void selectAllTables() {
		activateTableAndColumnsTab();
		new PushButton("Add...").click();
		new DefaultShell("Add Tables & Columns");
		DefaultTree dbTree = new DefaultTree();
		new WaitUntil(new TreeHasChildren(dbTree));
		Matcher[] jobs = new Matcher[1];
		jobs[0] = new IsEqual<String>("Fetching children of Database");
		new WaitWhile(new JobIsRunning(jobs));
		//AbstractWait.sleep(TimePeriod.NORMAL);
		dbTree.getItems().get(0).expand();
		new PushButton("Select all children").click();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Add Tables & Columns"));
	}

	
}
