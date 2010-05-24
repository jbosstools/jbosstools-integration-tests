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
		this.bot.menu("File").menu("New").menu("Other...").click();   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		this.bot.shell("New").activate(); //$NON-NLS-1$
		SWTBotTree importTree = this.bot.tree();
		importTree.expandNode("JBoss Tools Web").select("XHTML File");  //$NON-NLS-1$//$NON-NLS-2$
		this.bot.button(WidgetVariables.NEXT_BUTTON).click();
		this.bot.shell("New File XHTML"); //$NON-NLS-1$
		this.bot.textWithLabel("Name*").setText("test"); //$NON-NLS-1$ //$NON-NLS-2$
		this.bot.button(WidgetVariables.NEXT_BUTTON).click();
		this.bot.button(WidgetVariables.FINISH_BUTTON).click();
		assertEquals("Active Editor Title should be" ,"test.xhtml", this.bot.activeEditor().getTitle());  //$NON-NLS-1$ //$NON-NLS-2$
	}

}
