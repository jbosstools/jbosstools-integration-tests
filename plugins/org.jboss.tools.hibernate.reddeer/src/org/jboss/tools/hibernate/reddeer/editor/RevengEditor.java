package org.jboss.tools.hibernate.reddeer.editor;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
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
		new WaitUntil(new ShellWithTextIsActive("Add Tables & Columns"));
		// dynamic tree loading, TODO: impl. fine conditions
		AbstractWait.sleep(TimePeriod.NORMAL);
		new DefaultTree().getItems().get(0).expand();		
		// dynamic tree loading, TODO: impl. fine conditions
		AbstractWait.sleep(TimePeriod.NORMAL);
		new PushButton("Select all children").click();
		// dynamic tree updating, TODO: impl. fine conditions
		AbstractWait.sleep(TimePeriod.NORMAL);
		new PushButton("OK").click();
	}

	
}
