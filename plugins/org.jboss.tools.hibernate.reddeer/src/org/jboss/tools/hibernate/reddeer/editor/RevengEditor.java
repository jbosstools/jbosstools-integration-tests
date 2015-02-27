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

public class RevengEditor extends DefaultEditor {

	
	public void activateOverviewTab() {
		new DefaultCTabItem("Overview").activate();
	}
	
	public void activateTypeMappingsTab() {
		new DefaultCTabItem("Type Mappings").activate();
	}
	
	public void activateTableFiltersTab() {
		new DefaultCTabItem("Table Filters").activate();
	}
	
	public void  activateTableAndColumnsTab() {
		new DefaultCTabItem("Table  Columns").activate();
	}
	
	public void activateDesignTab() {
		new DefaultCTabItem("Design").activate();
	}
	
	public void activateSourceTab() {
		new DefaultCTabItem("Source").activate();
	}
	
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
