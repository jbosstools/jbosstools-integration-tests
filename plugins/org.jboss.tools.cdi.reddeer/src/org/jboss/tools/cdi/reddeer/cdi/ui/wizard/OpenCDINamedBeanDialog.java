package org.jboss.tools.cdi.reddeer.cdi.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class OpenCDINamedBeanDialog{
	
	public void setNamedPrefix(String prefix) {
		new DefaultText().setText(prefix);
	}
	
	public List<String> matchingItems() {
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.NORMAL, false);
		List<String> matchingItems = new ArrayList<String>();
		int tableItemsCount = new DefaultTable().rowCount();
		for (int i = 0; i < tableItemsCount; i++) {
			String itemInTable =new DefaultTable().getItem(i).getText();
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
