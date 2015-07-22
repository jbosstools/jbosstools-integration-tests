package org.jboss.tools.ws.reddeer.ui.preferences;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * Represents Web Services > CXF 2.x Preferences page in Preferences dialog.
 * 
 * @author Radoslav Rabara
 *
 */
public class WsCxf2xPreferencePage extends PreferencePage {

	/**
	 * Constructs CXF 2.x Preference page
	 */
	public WsCxf2xPreferencePage() {
		super(new String[] {"Web Services", "CXF 2.x Preferences"});
	}

	/**
	 * Adds CXF runtime with the specified location (CXF Home) attribute.
	 * 
	 * @param cxfHome location of the CXF runtime to be added
	 */
	public void add(String cxfHome) {
		new PushButton("Add...").click();
		new DefaultText(0).setText(cxfHome);
		new PushButton("Finish").click();
	}

	/**
	 * Click on the button Remove
	 */
	public void remove() {
		new PushButton("Remove").click();
	}

	/**
	 * Removes CXF runtime with the specified location (CXF Home) attribute.
	 * 
	 * @param cxfHome location of the CXF runtime to be removed
	 */
	public void remove(String cxfHome) {
		Table table = new DefaultTable();
		for(int i=0;i<table.rowCount();i++) {
			TableItem item = table.getItem(i);
			if(item.getText(1).equals(cxfHome)) {
				table.select(i);
				remove();
				return;
			}
		}
		throw new IllegalArgumentException("Row with given CXF Home was not found in CXF 2.x Preference Page");
	}

	/**
	 * Selects CXF runtime with the specified location (CXF Home) attribute.
	 * 
	 * @param cxfHome location of the CXF runtime to be selected
	 */
	public void select(String cxfHome) {
		Table table = new DefaultTable();
		for(TableItem item : table.getItems()) {
			if(item.getText(1).equals(cxfHome)) {
				item.setChecked(true);
				return;
			}
		}
		throw new IllegalArgumentException("Row with given CXF Home was not found in CXF 2.x Preference Page");
	}
	
	/**
	 * Confirm the dialog
	 */
	public void ok() {
		new PushButton("OK").click();
	}
}
