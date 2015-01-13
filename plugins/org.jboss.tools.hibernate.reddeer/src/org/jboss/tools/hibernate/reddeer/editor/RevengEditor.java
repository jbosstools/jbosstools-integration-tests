package org.jboss.tools.hibernate.reddeer.editor;

import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
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
		new DefaultCTabItem("Table & Columns").activate();
	}
	
	public void activateDesignTab() {
		new DefaultCTabItem("Design").activate();
	}
	
	public void activateSourceTab() {
		new DefaultCTabItem("Source").activate();
	}

	
}
