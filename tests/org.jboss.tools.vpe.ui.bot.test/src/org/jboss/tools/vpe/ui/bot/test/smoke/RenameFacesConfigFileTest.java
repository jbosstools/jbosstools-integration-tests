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
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.helper.FileRenameHelper;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
/**
 * Test renaming of faces-config.xml file
 * @author Vladimir Pakan
 *
 */
public class RenameFacesConfigFileTest extends VPEEditorTestCase{
  
  private static final String NEW_FACES_CONFIG_FILE_NAME = "faces-config-renamed.xml";
  private static final String OLD_FACES_CONFIG_FILE_NAME = "faces-config.xml";

	public void testRenameFacesConfigFile() throws Throwable{
		
	  checkRenameFacesConfigFile();
	  
	  setException(null);
		
	}
	/**
	 * Check renaming of faces-config.xml file
	 */
	private void checkRenameFacesConfigFile(){
	  
    openWebProjects();
    
    delay();
    
    SWTBot webProjects = bot.viewByTitle(WidgetVariables.WEB_PROJECTS).bot();
    SWTBotTree tree = webProjects.tree();

    tree.setFocus();
    String checkResult = FileRenameHelper.checkFileRenamingWithinWebProjects(bot, OLD_FACES_CONFIG_FILE_NAME, NEW_FACES_CONFIG_FILE_NAME,
      new String[]{JBT_TEST_PROJECT_NAME,IDELabel.WebProjectsTree.CONFIGURATION});
    assertNull(checkResult,checkResult);
    // web.xml file was properly modified
    SWTBotTreeItem configFilesTreeItem = tree
      .getTreeItem(JBT_TEST_PROJECT_NAME)
      .expand()
      .getNode(IDELabel.WebProjectsTree.WEB_XML)
      .expand()
      .getNode(IDELabel.WebProjectsTree.CONTEXT_PARAMS)
      .expand()
      .getNode(IDELabel.WebProjectsTree.JAVAX_FACES_CONFIG_FILES);
    
    ContextMenuHelper.prepareTreeItemForContextMenu(tree,configFilesTreeItem);
    new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.PROPERTIES, true)).click();
    bot.shell(IDELabel.Shell.PROPERTIES).activate();
    SWTBotTable propertiesTable = bot.table(); 
    String fullConfigFileName = propertiesTable.cell(propertiesTable.indexOf(IDELabel.PropertiesDialog.PARAM_VALUE, 0), 1);
    bot.button(IDELabel.Button.CLOSE).click();
    assertTrue(NEW_FACES_CONFIG_FILE_NAME + " Name of " 
        + OLD_FACES_CONFIG_FILE_NAME 
        + " file was not changed in web.xml file.",
      fullConfigFileName.endsWith(NEW_FACES_CONFIG_FILE_NAME));
	}
}