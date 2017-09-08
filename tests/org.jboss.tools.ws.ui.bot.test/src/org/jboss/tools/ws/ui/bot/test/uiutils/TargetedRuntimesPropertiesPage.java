package org.jboss.tools.ws.ui.bot.test.uiutils;

import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;

/**
 * 
 * @author Radoslav Rabara
 *
 */
public class TargetedRuntimesPropertiesPage {

	/**
	 * Selects this page in {@link PropertiesDialog} 
	 */
	public void select() {
		for(TreeItem ti : new DefaultTree().getItems()) {
			if(ti.getCell(0).equals("Targeted Runtimes")) {
				ti.doubleClick();
				return;
			}
		}
		throw new IllegalArgumentException("page was not found");
	}

	public void setSelectAllRuntimes(boolean check) {
		new CheckBox("Show all runtimes").toggle(check);
	}

	public void checkRuntime(String runtimeName, boolean check) {
		Table runtimes = new DefaultTable();
		for(TableItem ti : runtimes.getItems()) {
			if(ti.getText().equals(runtimeName)) {
				ti.setChecked(check);
				return;
			}
		}
		throw new IllegalArgumentException("Runtime '" + runtimeName + "' was not found");
	}

	public void checkAllRuntimes(boolean check) {
		Table runtimes = new DefaultTable();
		for(TableItem ti : runtimes.getItems()) {
			ti.setChecked(check);
		}
	}
}
