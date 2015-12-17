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

import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jst.reddeer.jsp.ui.wizard.NewJSPFileWizardDialog;
import org.jboss.tools.jst.reddeer.jsp.ui.wizard.NewJSPFileWizardJSPPage;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

public class AddSubstitutedELExpressionFolderScopeTest extends SubstitutedELTestCase {

	private static final String TEST_PAGE_FOR_FOLDER = "testPage"; //$NON-NLS-1$
	private static final String TEST_FOLDER = "testFolder"; //$NON-NLS-1$
	private static String originalEditorText;
	private static TextEditor editor;

	// Do not edit this variable as test will fail
	private static final String EL_VALUE = "Any Name"; //$NON-NLS-1$

	@Test
	public void testAddSubstitutedELExpressionFolderScope() throws Throwable {
		// Test open page
		openPage();
		editor = new TextEditor(TEST_PAGE);
		originalEditorText = editor.getText();
		// Test create new folder
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent").select();
		new ShellMenu("File", "New", "Folder").select();
		new DefaultShell("New Folder");
		new LabeledText("Folder name:").setText(TEST_FOLDER);
		new FinishButton().click();
		// Test create page in new folder
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", TEST_FOLDER).select();
		NewJSPFileWizardDialog newJSPFileWizardDialog = new NewJSPFileWizardDialog();
		newJSPFileWizardDialog.open();
		NewJSPFileWizardJSPPage newJSPFileWizardJSPPage = new NewJSPFileWizardJSPPage();
		newJSPFileWizardJSPPage.setFileName(TEST_PAGE_FOR_FOLDER);
		newJSPFileWizardDialog.finish();
		TextEditor editorForTestPage = new TextEditor(TEST_PAGE_FOR_FOLDER + ".jsp");
		editorForTestPage.setText(originalEditorText);
		editorForTestPage.save();
		editorForTestPage.close();
		editor.activate();
		// Test open Page Design Options
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell(PAGE_DESIGN);
		// Test choose Substituted EL tab
		new DefaultTabItem(SUBSTITUTED_EL).activate();
		// Clear EL table
		clearELTable(new DefaultTable());
		// Test add EL with folder scope to list
		new PushButton("Add").click();
		new DefaultShell(ADD_EL);
		new LabeledText("El Name*").setText("user.name");
		new LabeledText("Value").setText(EL_VALUE);
		new RadioButton("Folder: Any Page at the Same Folder").click();
		new FinishButton().click();
		// Test check table with ELs
		new DefaultShell(PAGE_DESIGN);
		Table table = new DefaultTable();
		String elName = table.getItem(0).getText(1);
		String scope = table.getItem(0).getText(0);
		String elValue = table.getItem(0).getText(2);
		assertEquals("user.name", elName); //$NON-NLS-1$
		assertEquals("Folder", scope); //$NON-NLS-1$
		assertEquals(EL_VALUE, elValue);
		// Test close Design Options
		new OkButton().click();
		// Check page content
		editor.activate();
		assertVisualEditorContains(new SWTBotWebBrowser(TEST_PAGE), "INPUT", new String[] { "value" },
				new String[] { EL_VALUE }, TEST_PAGE);
		openPage("hello.jsp");
		assertVisualEditorContainsNodeWithValue(new SWTBotWebBrowser("hello.jsp"), EL_VALUE, "hello.jsp");
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME)
				.getProjectItem("WebContent", TEST_FOLDER, TEST_PAGE_FOR_FOLDER + ".jsp").open();
		assertVisualEditorContains(new SWTBotWebBrowser(TEST_PAGE_FOR_FOLDER + ".jsp"), "INPUT",
				new String[] { "value" }, new String[] { "#{user.name}" }, TEST_PAGE_FOR_FOLDER + ".jsp");
		editor.activate();
		// Test open Page Design Options
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell(PAGE_DESIGN);
		// Test choose Substituted EL tab
		new DefaultTabItem(SUBSTITUTED_EL).activate();
		// Delete item
		new DefaultTableItem(0).select();
		new PushButton("Remove").click();
		new OkButton().click();
		// Check VPE content
		assertVisualEditorContains(new SWTBotWebBrowser(TEST_PAGE), "INPUT", new String[] { "value" },
				new String[] { "#{user.name}" }, TEST_PAGE);

		assertVisualEditorContainsNodeWithValue(new SWTBotWebBrowser("hello.jsp"), "#{user.name}", "hello.jsp");

		assertVisualEditorContains(new SWTBotWebBrowser(TEST_PAGE_FOR_FOLDER + ".jsp"), "INPUT",
				new String[] { "value" }, new String[] { "#{user.name}" }, TEST_PAGE_FOR_FOLDER + ".jsp");
	}

	@Override
	public void tearDown() throws Exception {
		// Restore page state before tests
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", TEST_FOLDER).delete();
		editor.activate();
		editor.setText(originalEditorText);
		editor.save();
		super.tearDown();
	}

}
