/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.cdi.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.condition.TableHasRows;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.cdi.reddeer.condition.TableIsUpdating;

public class OpenCDINamedBeanDialog{
	
	public void setNamedPrefix(String prefix) {
		new DefaultText().setText(prefix);
	}
	
	public List<String> matchingItems() {
		Table itemsTable = new DefaultTable();
		new WaitUntil(new TableHasRows(itemsTable), TimePeriod.DEFAULT, false);
		new WaitWhile(new TableIsUpdating(itemsTable, TimePeriod.getCustom(4)));
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
		new WaitWhile(new ShellIsAvailable("Open CDI Named Bean"));
	}
	
	public void cancel() {
		new CancelButton().click();
		new WaitWhile(new ShellIsAvailable("Open CDI Named Bean"));
	}

}
