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
package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import java.util.List;

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.matcher.ColumnTableItemMatcher;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests Global EL Variables Definition
 * 
 * @author vlado pakan
 *
 */
public class GlobalELVariablesTest extends VPEEditorTestCase {

	private static final String elName = "user.name";
	private static final String elValue = "!!TestELValue!!";
	private static String FACELET_PROJECT_XHTML_FILE_NAME = "globalELVariablesTest.xhtml";
	@Test
	public void testGlobalELVariables() throws Throwable {
		WorkbenchPreferenceDialog preferencesDialog = new WorkbenchPreferenceDialog();
		preferencesDialog.open();
		new DefaultTreeItem("JBoss Tools", "Web", "Expression Language","Variables").select();
		new PushButton("Add").click();
		new DefaultShell("Add EL Reference");
		new LabeledText("El Name*").setText(GlobalELVariablesTest.elName);
		new LabeledText("Value").setText(GlobalELVariablesTest.elValue);
		new FinishButton().click();
		preferencesDialog.ok();
		openPage(VPEAutoTestCase.TEST_PAGE);
		// Create XHTML File in Facelet Project
		packageExplorer.open();
		packageExplorer.getProject(VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages")
				.select();
		// add XHTML
		createXhtmlPage(GlobalELVariablesTest.FACELET_PROJECT_XHTML_FILE_NAME);
		new WaitWhile(new JobIsRunning());
		TextEditor xhtmlEditor = new TextEditor(GlobalELVariablesTest.FACELET_PROJECT_XHTML_FILE_NAME);
		xhtmlEditor.setText("<html xmlns=\"http://www.w3.org/1999/xhtml\"\n"
				+ "           xmlns:h=\"http://java.sun.com/jsf/html\">\n" + "  <body>\n"
				+ "    <h:outputText value=\"#{user.name}\"/>\n" + "  </body>\n" + "</html>");
		xhtmlEditor.save();
		new WaitWhile(new JobIsRunning());
		assertVisualEditorContainsNodeWithValue(
				new SWTBotWebBrowser(GlobalELVariablesTest.FACELET_PROJECT_XHTML_FILE_NAME),
				GlobalELVariablesTest.elValue, GlobalELVariablesTest.FACELET_PROJECT_XHTML_FILE_NAME);
		xhtmlEditor.close();
		TextEditor jspEditor = new TextEditor(VPEAutoTestCase.TEST_PAGE);
		assertVisualEditorContains(new SWTBotWebBrowser(VPEAutoTestCase.TEST_PAGE), "INPUT", new String[] { "value" },
				new String[] { GlobalELVariablesTest.elValue }, VPEAutoTestCase.TEST_PAGE);
		jspEditor.close();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void tearDown() throws Exception {
		WorkbenchPreferenceDialog preferencesDialog = new WorkbenchPreferenceDialog();
		preferencesDialog.open();
		new DefaultTreeItem("JBoss Tools", "Web", "Expression Language","Variables").select();

		List<TableItem> tableItems = new DefaultTable().getItems(new ColumnTableItemMatcher(0, "Global"),
				new ColumnTableItemMatcher(1, GlobalELVariablesTest.elName),
				new ColumnTableItemMatcher(2, GlobalELVariablesTest.elValue));
		if (tableItems != null && tableItems.size() > 0) {
			tableItems.get(0).select();
			new PushButton("Remove").click();
		}
		preferencesDialog.ok();
		super.tearDown();
	}
}
