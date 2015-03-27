package org.jboss.tools.cdi.reddeer.cdi.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;

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
