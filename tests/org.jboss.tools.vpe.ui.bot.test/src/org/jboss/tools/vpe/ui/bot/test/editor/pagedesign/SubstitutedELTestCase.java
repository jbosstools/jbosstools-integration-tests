/******************************************************************************* 
 * Copyright (c) 2012 - 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;

public abstract class SubstitutedELTestCase extends PageDesignTestCase{
	
	static final String ADD_EL = "Add EL Reference"; //$NON-NLS-1$
	static final String SUBSTITUTED_EL = "Substituted EL expressions"; //$NON-NLS-1$
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	public void tearDown() throws Exception {
		new DefaultToolItem(PAGE_DESIGN).click();;
		Shell dialogShell = new DefaultShell(PAGE_DESIGN);
		// Test choose Substituted EL tab
		new DefaultTabItem(SUBSTITUTED_EL).activate();
		clearELTable(new DefaultTable());
		try {
			new OkButton().click(); //$NON-NLS-1$
		} catch (RedDeerException rde) {
			dialogShell.close();
		}
		super.tearDown();
	}
	
	void clearELTable(Table table) {
		while (table.rowCount() > 0) {
			table.select(0);
			new PushButton("Remove").click();
		}
	}
		
}
