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

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

/**
 * @author mareshkau
 *
 */
public class NewXHTMLPageWizardTest extends VPEAutoTestCase{

	@Override
	protected void closeUnuseDialogs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void testNewXHTMLPageWizard() {
		/*
		 * Open wizard page
		 */
	  open.newObject(ActionItem.NewObject.JBossToolsWebXHTMLFile.LABEL);
    bot.shell(IDELabel.Shell.NEW_XHTML_FILE).activate();
    bot.textWithLabel(ActionItem.NewObject.JBossToolsWebXHTMLFile.TEXT_FILE_NAME).setText("test");
    bot.button(IDELabel.Button.NEXT).click();
    bot.checkBox(IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX).select();
    bot.table().select(IDELabel.NewXHTMLFileDialog.TEMPLATE_FACELET_FORM_XHTML_NAME);
    bot.button(IDELabel.Button.FINISH).click();
		assertEquals("Active Editor Title should be" ,"test.xhtml", this.bot.activeEditor().getTitle());  //$NON-NLS-1$ //$NON-NLS-2$
	}

}
