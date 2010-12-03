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

import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
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

	/**
	 * Test new xhtml page wizard basic functionality.
	 */
	public void testNewXHTMLPageWizard() {
		/*
		 * Open wizard page
		 */
		open.newObject(ActionItem.NewObject.JBossToolsWebXHTMLFile.LABEL);
		bot.shell(IDELabel.Shell.NEW_XHTML_FILE).activate();
		bot.textWithLabel(ActionItem.NewObject.JBossToolsWebXHTMLFile.TEXT_FILE_NAME).setText("test"); //$NON-NLS-1$
		bot.textWithLabel(
				ActionItem.NewObject.JBossToolsWebXHTMLFile.TEXT_ENTER_OR_SELECT_THE_PARENT_FOLDER)
				.setText(JBT_TEST_PROJECT_NAME + "/WebContent/pages"); //$NON-NLS-1$
		bot.button(IDELabel.Button.NEXT).click();
		/*
		 * Check that the checkbox is disabled by default
		 */
		assertFalse(
				"'" + IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX //$NON-NLS-1$
				+ "' checkbox should be disabled by default", //$NON-NLS-1$
				bot.checkBox(
						IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX)
						.isChecked());
		bot.checkBox(IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX).select();
		bot.table().select(IDELabel.NewXHTMLFileDialog.TEMPLATE_FACELET_FORM_XHTML_NAME);
		bot.button(IDELabel.Button.FINISH).click();
		bot.sleep(Timing.time2S());
		assertEquals("Active Editor Title should be" ,"test.xhtml", this.bot.activeEditor().getTitle()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Test blank result without any template text.
	 * Tests https://jira.jboss.org/browse/JBIDE-6921
	 */
	public void testBlankResultWithoutAnyTemplateText_JBIDE6921() {
		open.newObject(ActionItem.NewObject.JBossToolsWebXHTMLFile.LABEL);
		bot.shell(IDELabel.Shell.NEW_XHTML_FILE).activate();
		bot.textWithLabel(ActionItem.NewObject.JBossToolsWebXHTMLFile.TEXT_FILE_NAME).setText("test2"); //$NON-NLS-1$
		bot.textWithLabel(
				ActionItem.NewObject.JBossToolsWebXHTMLFile.TEXT_ENTER_OR_SELECT_THE_PARENT_FOLDER)
				.setText(JBT_TEST_PROJECT_NAME + "/WebContent/pages"); //$NON-NLS-1$
		bot.button(IDELabel.Button.NEXT).click();
		/*
		 * Check that the checkbox is stored between the dialog's launches
		 */
		assertTrue(
				"'" + IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX //$NON-NLS-1$
				+ "' checkbox should be enabled (after previous dialog call)", //$NON-NLS-1$
				bot.checkBox(
						IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX)
						.isChecked());
		/*
		 * Make some click on the checkbox and leave it disabled
		 */
		bot.checkBox(IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX).select();
		bot.checkBox(IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX).deselect();
		bot.checkBox(IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX).select();
		bot.checkBox(IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX).deselect();
		bot.button(IDELabel.Button.FINISH).click();
		assertEquals("Active Editor Title should be" ,"test2.xhtml", this.bot.activeEditor().getTitle());  //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Created XHTML file should be blank" ,"", this.bot.activeEditor().toTextEditor().getText());  //$NON-NLS-1$ //$NON-NLS-2$

	}

}
