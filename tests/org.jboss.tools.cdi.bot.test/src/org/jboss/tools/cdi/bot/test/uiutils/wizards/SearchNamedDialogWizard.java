/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.uiutils.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class SearchNamedDialogWizard extends Wizard {

	private List<String> matchingItems = null;
	
	public SearchNamedDialogWizard() {
		super(new SWTBot().activeShell().widget);
		assert (IDELabel.Menu.OPEN_CDI_NAMED_BEAN).equals(getText());	
		matchingItems = new ArrayList<String>();
	}
	
	public SearchNamedDialogWizard setNamedPrefix(String prefix) {
		bot().text().setText(prefix);
		bot().sleep(Timing.time2S());
		return this;
	}
	
	public void ok() {
		clickButton("OK");		
		bot().sleep(Timing.time1S());
	}
	
	public SearchNamedDialogWizard setMatchingItems(String... items) {
		bot().table(0).select(items);
		return this;
	}
	
	public List<String> matchingItems() {
		AbstractWait.sleep(5000);
		int tableItemsCount = new DefaultTable().rowCount();
		for (int i = 0; i < tableItemsCount; i++) {
			String itemInTable =new DefaultTable().getItem(i).getText();
			if (itemInTable.contains("Workspace matches")) continue;
			matchingItems.add(itemInTable);
		}
		return matchingItems;
	}
	
}
