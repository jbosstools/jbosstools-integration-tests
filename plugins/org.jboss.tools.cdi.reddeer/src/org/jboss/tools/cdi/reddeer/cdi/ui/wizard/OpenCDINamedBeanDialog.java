package org.jboss.tools.cdi.reddeer.cdi.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.cdi.reddeer.condition.TableIsUpdating;

public class OpenCDINamedBeanDialog{
	
	public void setNamedPrefix(String prefix) {
		new DefaultText().setText(prefix);
	}
	
	public List<String> matchingItems() {
		Table itemsTable = new DefaultTable();
		new WaitUntil(new TableHasRows(itemsTable), TimePeriod.NORMAL, false);
		new WaitWhile(new TableIsUpdating(itemsTable, TimePeriod.getCustom(2)));
		List<String> matchingItems = new ArrayList<String>();
		int tableItemsCount = itemsTable.rowCount();
		for (int i = 0; i < tableItemsCount; i++) {
			String itemInTable =itemsTable.getItem(i).getText();
			if (itemInTable.contains("Workspace matches")) continue;
			matchingItems.add(itemInTable);
		}
		return matchingItems;
	}
	
	public void setMatchingItems(String... items) {
		new DefaultTable().select(items);
	}
	
	public void ok() {
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive("Open CDI Named Bean"));
	}
	
	public void cancel() {
		new CancelButton().click();
		new WaitWhile(new ShellWithTextIsActive("Open CDI Named Bean"));
	}

}
