/*******************************************************************************

 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests manipulating of EL value  
 * @author vlado pakan
 *
 */
public class ManipulatingELValueTest extends PageDesignTestCase {
  
  private SWTBotExt botExt = null;
  private static final String TEST_IN_PAGE_FOLDER_PAGE_NAME = "testInPageFolder.jsp";
  private static final String TEST_IN_MAIN_FOLDER_PAGE_NAME = "testInMainFolder.jsp";
  private static final String EL_VARIABLE_NAME = "test.variable";
  private static final String EL_IN_PAGE_FOLDER_VARIABLE_VALUE = "EL test value in Page folder";
  private static final String EL_IN_MAIN_FOLDER_VARIABLE_VALUE = "EL test value in main folder";
  private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
    "<html>\n" +
    "  <body>\n" +
    "    <h:outputText value = \"#{" + ManipulatingELValueTest.EL_VARIABLE_NAME + "}\"/>\n" +
    "  </body>\n" +
    "</html>";
  private SWTBotEclipseEditor testInPageFolderEditor;
  private SWTBotEclipseEditor testInMainFolderEditor;
  
	public ManipulatingELValueTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	public void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
	}
	/**
	 * Tests manipulating of EL value 
	 */
	public void testManipulatingELValue(){
	  createJspPage(ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME);
	  testInPageFolderEditor = botExt.editorByTitle(ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME).toTextEditor();
	  testInPageFolderEditor.setText(ManipulatingELValueTest.PAGE_TEXT);
	  testInPageFolderEditor.save();
	  createJspPage(ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME , JBT_TEST_PROJECT_NAME , "WebContent");
    testInMainFolderEditor = botExt.editorByTitle(ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME).toTextEditor();
    testInMainFolderEditor.setText(ManipulatingELValueTest.PAGE_TEXT);
    testInMainFolderEditor.save();
    testInPageFolderEditor.show();
    addELSubstitution(ManipulatingELValueTest.EL_VARIABLE_NAME, ManipulatingELValueTest.EL_IN_PAGE_FOLDER_VARIABLE_VALUE,
        IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_SCOPE_FOLDER);
    testInPageFolderEditor.save();
    bot.sleep(Timing.time2S());
    SWTBotWebBrowser webBrowserInPageFolder = new SWTBotWebBrowser(ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME, botExt);
    assertVisualEditorContainsNodeWithValue(webBrowserInPageFolder,
        ManipulatingELValueTest.EL_IN_PAGE_FOLDER_VARIABLE_VALUE,
        ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME);
    testInMainFolderEditor.show();
    addELSubstitution(ManipulatingELValueTest.EL_VARIABLE_NAME, ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE,
        IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_SCOPE_FOLDER);
    testInMainFolderEditor.save();
    bot.sleep(Timing.time2S());
    SWTBotWebBrowser webBrowserInMainFolder = new SWTBotWebBrowser(ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME, botExt);
    assertVisualEditorContainsNodeWithValue(webBrowserInMainFolder,
        ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE,
        ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME);
    // Edit EL value from Main Folder Page
    testInMainFolderEditor.show();
    editELSubstitution(ManipulatingELValueTest.EL_VARIABLE_NAME,
        IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_FOLDER_SCOPE_TABLE_LABEL,
        ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE,
        IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_SCOPE_PROJECT);
    assertVisualEditorContainsNodeWithValue(webBrowserInMainFolder,
        ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE,
        ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME);
    testInPageFolderEditor.show();
    assertVisualEditorContainsNodeWithValue(webBrowserInPageFolder,
        ManipulatingELValueTest.EL_IN_PAGE_FOLDER_VARIABLE_VALUE,
        ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME);
    // Delete Folder scoped definition of EL Variable
    deleteELSubstitution(ManipulatingELValueTest.EL_VARIABLE_NAME,
        IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_FOLDER_SCOPE_TABLE_LABEL);
    assertVisualEditorContainsNodeWithValue(webBrowserInMainFolder,
        ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE,
        ManipulatingELValueTest.TEST_IN_MAIN_FOLDER_PAGE_NAME);
    assertVisualEditorContainsNodeWithValue(webBrowserInPageFolder,
        ManipulatingELValueTest.EL_IN_MAIN_FOLDER_VARIABLE_VALUE,
        ManipulatingELValueTest.TEST_IN_PAGE_FOLDER_PAGE_NAME);
  }

	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  @Override
public void tearDown() throws Exception {

    deleteAllELSubstitutions();
    
    if (testInPageFolderEditor != null){
      testInPageFolderEditor.close();
    }
    if (testInMainFolderEditor != null){
      testInMainFolderEditor.close();  
    }
    
    super.tearDown();
  } 
}
