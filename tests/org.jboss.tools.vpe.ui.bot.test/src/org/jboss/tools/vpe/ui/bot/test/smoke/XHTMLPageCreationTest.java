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
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
/**
 * Test XHTML page Creation and Saving
 * @author Vladimir Pakan
 *
 */
public class XHTMLPageCreationTest extends VPEEditorTestCase{
  
  public static final String TEST_NEW_XHTML_FILE_NAME = "TestXHTML.xhtml";
  private static final String SAVE_COMMENT = "<!-- Save This -->\n";
  private static final String DO_NOT_SAVE_COMMENT = "<!-- Do not Save This -->\n";

  public void testXHTMLPageCreation() throws Throwable{
    
    checkXHTMLPageCreation();
    
    setException(null);
    
  }
  
	/**
	 * Test XHTML page Creation and Saving
	 */
	private void checkXHTMLPageCreation(){
	  
	  openWebProjects();
	  
	  delay();
	  
	  SWTBot webProjects = bot.viewByTitle(WidgetVariables.WEB_PROJECTS).bot();
	  SWTBotTree tree = webProjects.tree();

    tree.setFocus();
    // tree of webProject view is not populated properly. Project node has to be reexpanded
    SWTBotTreeItem projectTreeItem = tree.getTreeItem(JBT_TEST_PROJECT_NAME);
    projectTreeItem.expand();
    projectTreeItem.collapse();
    projectTreeItem.expand();
    
    SWTBotTreeItem webContentTreeItem = projectTreeItem.expand().getNode(IDELabel.WebProjectsTree.WEB_CONTENT);
    
    webContentTreeItem.select();
    // create new JSP file
    open.newObject(ActionItem.NewObject.JBossToolsWebXHTMLFile.LABEL);
    bot.shell(IDELabel.Shell.NEW_XHTML_FILE).activate();
    bot.textWithLabel(ActionItem.NewObject.JBossToolsWebXHTMLFile.TEXT_FILE_NAME).setText(TEST_NEW_XHTML_FILE_NAME);
    bot.button(IDELabel.Button.NEXT).click();
    bot.checkBox(IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX).select();
    bot.table().select(IDELabel.NewXHTMLFileDialog.TEMPLATE_FACELET_FORM_XHTML_NAME);
    bot.button(IDELabel.Button.FINISH).click();
    webContentTreeItem.expand();
    bot.sleep(Timing.time1S());
    SWTBotTreeItem xhtmlTestPageTreeItem = webContentTreeItem.getNode(TEST_NEW_XHTML_FILE_NAME);
    
    String checkResult = CheckFileChangesSaving.checkIt(bot,bot.editorByTitle(TEST_NEW_XHTML_FILE_NAME).toTextEditor(), tree, xhtmlTestPageTreeItem,
      SAVE_COMMENT, true);
    
    assertNull(checkResult,checkResult);
    
    checkResult = CheckFileChangesSaving.checkIt(bot,bot.editorByTitle(TEST_NEW_XHTML_FILE_NAME).toTextEditor(), tree, xhtmlTestPageTreeItem,
      DO_NOT_SAVE_COMMENT, false);
    
    assertNull(checkResult,checkResult);
    
    delay();
    
    bot.editorByTitle(TEST_NEW_XHTML_FILE_NAME).toTextEditor().close();

	}
}