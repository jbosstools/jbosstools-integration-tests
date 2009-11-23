/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.smoke;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
/**
 * Test JSP page Creation and Saving
 * @author Vladimir Pakan
 *
 */
public class JSPPageCreationTest extends VPEEditorTestCase{
  
  public static final String TEST_NEW_JSP_FILE_NAME = "TestJSP.jsp";
  private static final String SAVE_COMMENT = "<!-- Save This -->\n";
  private static final String DO_NOT_SAVE_COMMENT = "<!-- Do not Save This -->\n";
  
	public void testEditorJSPPageCreation() throws Throwable{
		
		checkJSPPageCreation();
	  
	  setException(null);
		
	}
	/**
   * Test JSP page Creation and Saving
   */
  private void checkJSPPageCreation(){
    
    openWebProjects();
    
    delay();
    
    SWTBot webProjects = bot.viewByTitle(WidgetVariables.WEB_PROJECTS).bot();
    SWTBotTree tree = webProjects.tree();

    tree.setFocus();

    SWTBotTreeItem webContentTreeItem = tree
      .getTreeItem(JBT_TEST_PROJECT_NAME)
      .expand()
        .getNode(IDELabel.WebProjectsTree.WEB_CONTENT);
    
    webContentTreeItem.select();
    // create new JSP file
    bot.menu(IDELabel.Menu.FILE).menu(IDELabel.Menu.NEW).menu(IDELabel.Menu.JSP_FILE).click();
    bot.shell(IDELabel.Shell.NEW_JSP_FILE).activate();
    bot.textWithLabel(IDELabel.NewJSPFileDialog.NAME).setText(TEST_NEW_JSP_FILE_NAME);
    bot.comboBoxWithLabel(IDELabel.NewJSPFileDialog.TEMPLATE).setText(IDELabel.NewJSPFileDialog.TEMPLATE_JSF_BASE_PAGE);
    bot.button(IDELabel.Button.FINISH).click();
    
    SWTBotTreeItem jspTestPageTreeItem = webContentTreeItem.getNode(TEST_NEW_JSP_FILE_NAME);
    
    String checkResult = CheckFileChangesSaving.checkIt(bot, bot.editorByTitle(TEST_NEW_JSP_FILE_NAME).toTextEditor(),
      tree, jspTestPageTreeItem,
      SAVE_COMMENT, true);
    
    assertNull(checkResult,checkResult);
    
    checkResult = CheckFileChangesSaving.checkIt(bot, bot.editorByTitle(TEST_NEW_JSP_FILE_NAME).toTextEditor(),
      tree, jspTestPageTreeItem,
      DO_NOT_SAVE_COMMENT, false);
    
    assertNull(checkResult,checkResult);
    
    delay();
    
    bot.editorByTitle(TEST_NEW_JSP_FILE_NAME).toTextEditor().close();

  }


}