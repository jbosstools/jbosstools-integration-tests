/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;

public abstract class SubstitutedELTestCase extends PageDesignTestCase{
	
	static final String ADD_EL = "Add EL Reference"; //$NON-NLS-1$
	static final String SUBSTITUTED_EL = "Substituted EL expressions"; //$NON-NLS-1$

	@Override
	protected void closeUnuseDialogs() {
		try {
			bot.shell(ADD_EL).close();
		} catch (WidgetNotFoundException e) {
		}
		super.closeUnuseDialogs();
	}
	
	@Override
	protected boolean isUnuseDialogOpened() {
		boolean isOpened = super.isUnuseDialogOpened();
		try {
			bot.shell(ADD_EL).activate();
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	public void tearDown() throws Exception {
		bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
		bot.shell(PAGE_DESIGN).activate();
		bot.tabItem(SUBSTITUTED_EL).activate();
		clearELTable(bot.table());
		try {
			bot.button("OK").click(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
			bot.shell(PAGE_DESIGN).close();
		}
		super.tearDown();
	}
	
	void clearELTable(SWTBotTable table){
		try {
			while (true) {
				table.select(0);
				bot.button("Remove").click(); //$NON-NLS-1$
			}
		} catch (IllegalArgumentException e) {
		}
		catch (WidgetNotFoundException e) {
		}
	}
		
}
