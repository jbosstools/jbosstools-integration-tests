package org.jboss.tools.forge.reddeer.ui.wizard;

import java.util.List;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

/**
 * Reddeer implementation of forge Entities from tables wizard second page
 * @author jrichter
 *
 */
public class EntitiesFromTablesWizardSecondPage extends WizardPage {
	
	/**
	 * Selects tables with given names
	 * @param tableNames names of the tables to select 
	 */
	public void selectTables(String... tableNames) {
		Table table = new DefaultTable();
		table.select(tableNames);
	}
	
	/**
	 * Select all tables
	 */
	public void selectAll() {
		new PushButton("Select All").click();
	}
	
	/**
	 * Deselect all tables
	 */
	public void selectNone() {
		new PushButton("Select None").click();
	}
	
	/**
	 * Get all listed tables
	 * @return List of TableItems representing the tables
	 */
	public List<TableItem> getAllTables() {
		return new DefaultTable().getItems();
	}
}
