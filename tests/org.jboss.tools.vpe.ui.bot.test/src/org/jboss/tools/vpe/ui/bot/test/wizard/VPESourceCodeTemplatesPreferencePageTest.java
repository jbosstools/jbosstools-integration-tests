/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.wizard;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;

/**
 * This test class open vpe preference page
 * Window->Preferences->JBoss Tools->Web->Editors->Visual Page Editor->Templates
 * 
 * @author mareshkau
 *
 */
public class VPESourceCodeTemplatesPreferencePageTest extends SWTBotTestCase{

	//just open a VPE Source Code templates preference test page
	public void testSourceCodeTemplatesPreferencePage() {
		this.bot.menu(IDELabel.Menu.WINDOW).menu(IDELabel.Menu.PREFERENCES).click(); //$NON-NLS-1$ //$NON-NLS-2$
		SWTBotTree preferenceTree = this.bot.tree();
		preferenceTree
		.expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS) //$NON-NLS-1$
		.expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB) //$NON-NLS-1$
		.expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB_EDITORS) //$NON-NLS-1$
		.expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB_EDITORS_VPE).select(); //$NON-NLS-1$
		bot.tabItem(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB_EDITORS_VPE_VISUAL_TEMPLATES).activate(); //$NON-NLS-1$
		try{
			this.bot.button(IDELabel.Button.ADD).click(); //$NON-NLS-1$
			this.bot.button(IDELabel.Button.CANCEL).click(); //$NON-NLS-1$
		} catch(WidgetNotFoundException ex){
			fail("Preference Page has not been created"+ex);//$NON-NLS-1$
		}finally{
			this.bot.button(WidgetVariables.OK_BUTTON).click();
		}
	}
}
