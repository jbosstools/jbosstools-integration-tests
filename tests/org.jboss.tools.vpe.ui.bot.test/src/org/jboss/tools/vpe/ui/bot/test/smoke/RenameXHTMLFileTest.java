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
import org.jboss.tools.ui.bot.ext.helper.FileRenameHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;

/**
 * Test renaming of XHTML file
 * 
 * @author Vladimir Pakan
 * 
 */
public class RenameXHTMLFileTest extends VPEEditorTestCase {

  private static final String NEW_XHTML_FILE_NAME = "renamed-" 
    + XHTMLPageCreationTest.TEST_NEW_XHTML_FILE_NAME;
  
  public void testRenameFacesConfigFile() throws Throwable {

    checkRenameXHTMLFile();

    setException(null);
    
  }

  /**
   * Check renaming of faces-config.xml file
   */
  private void checkRenameXHTMLFile() {

    openWebProjects();

    delay();

    SWTBot webProjects = bot.viewByTitle(WidgetVariables.WEB_PROJECTS).bot();
    SWTBotTree tree = webProjects.tree();

    tree.setFocus();
    String checkResult = FileRenameHelper.checkFileRenamingWithinWebProjects(bot,
        XHTMLPageCreationTest.TEST_NEW_XHTML_FILE_NAME, NEW_XHTML_FILE_NAME,
        new String[] {JBT_TEST_PROJECT_NAME, IDELabel.WebProjectsTree.CONFIGURATION});
    assertNull(checkResult, checkResult);
    
  }
}