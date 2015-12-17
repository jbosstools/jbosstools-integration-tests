/*******************************************************************************

 * Copyright (c) 2007-2016 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests manipulating of EL value
 * 
 * @author vlado pakan
 *
 */
public class ManipulatingELValueTest extends PageDesignTestCase {
	private static final String TEST_IN_PAGE_FOLDER_PAGE_NAME = "testInPageFolder.jsp";
	private static final String TEST_IN_MAIN_FOLDER_PAGE_NAME = "testInMainFolder.jsp";
	private static final String EL_VARIABLE_NAME = "test.variable";
	private static final String EL_IN_PAGE_FOLDER_VARIABLE_VALUE = "EL test value in Page folder";
	private static final String EL_IN_MAIN_FOLDER_VARIABLE_VALUE = "EL test value in main folder";
	private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
			+ "<html>\n" + "  <body>\n" + "    <h:outputText value = \"#{" + ManipulatingELValueTest.EL_VARIABLE_NAME
			+ "}\"/>\n" + "  </body>\n" + "</html>";
	private TextEditor testInPageFolderEditor;
	private TextEditor testInMainFolderEditor;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
	}

	/**
	 * Tests manipulating of EL value
	 */
	@Test
	public void testManipulatingELValue() {
		createJspPage(ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME);
		testInPageFolderEditor = new TextEditor(ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME);
		testInPageFolderEditor.setText(ManipulatingELValueTest.PAGE_TEXT);
		testInPageFolderEditor.save();
		createJspPage(ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME, JBT_TEST_PROJECT_NAME, "WebContent");
		testInMainFolderEditor = new TextEditor(ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME);
		testInMainFolderEditor.setText(ManipulatingELValueTest.PAGE_TEXT);
		testInMainFolderEditor.save();
		testInPageFolderEditor.activate();
		addELSubstitution(ManipulatingELValueTest.EL_VARIABLE_NAME,
				ManipulatingELValueTest.EL_IN_PAGE_FOLDER_VARIABLE_VALUE, "Folder: Any Page at the Same Folder");
		testInPageFolderEditor.save();
		SWTBotWebBrowser webBrowserInPageFolder = new SWTBotWebBrowser(
				ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowserInPageFolder,
				ManipulatingELValueTest.EL_IN_PAGE_FOLDER_VARIABLE_VALUE,
				ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME);
		testInMainFolderEditor.activate();
		addELSubstitution(ManipulatingELValueTest.EL_VARIABLE_NAME,
				ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE, "Folder: Any Page at the Same Folder");
		testInMainFolderEditor.save();
		SWTBotWebBrowser webBrowserInMainFolder = new SWTBotWebBrowser(
				ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowserInMainFolder,
				ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE,
				ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME);
		// Edit EL value from Main Folder Page
		testInMainFolderEditor.activate();
		editELSubstitution(ManipulatingELValueTest.EL_VARIABLE_NAME, "Folder",
				ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE, "Project: Any Page at the Same Project");
		assertVisualEditorContainsNodeWithValue(webBrowserInMainFolder,
				ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE,
				ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME);
		testInPageFolderEditor.activate();
		assertVisualEditorContainsNodeWithValue(webBrowserInPageFolder,
				ManipulatingELValueTest.EL_IN_PAGE_FOLDER_VARIABLE_VALUE,
				ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME);
		// Delete Folder scoped definition of EL Variable
		deleteELSubstitution(ManipulatingELValueTest.EL_VARIABLE_NAME, "Folder");
		assertVisualEditorContainsNodeWithValue(webBrowserInMainFolder,
				ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE,
				ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowserInPageFolder,
				ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE,
				ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME);
	}

	@Override
	public void tearDown() throws Exception {

		deleteAllELSubstitutions();

		if (testInPageFolderEditor != null) {
			testInPageFolderEditor.close();
		}
		if (testInMainFolderEditor != null) {
			testInMainFolderEditor.close();
		}

		super.tearDown();
	}
}
