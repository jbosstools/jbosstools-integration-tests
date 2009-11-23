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

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
/**
 * Test VPE Editor synchronization with Properties and Outline View
 * @author Vladimir Pakan
 *
 */
public class EditorSynchronizationTest extends VPEEditorTestCase{
  
	public void testEditorSynchronization() throws Throwable{
		
	  openPage();
		openOutlineView();
		openPropertiesView();
		checkVPEEditorSynchronization();
	  
	  setException(null);
		
	}
	/**
	 * Check VPE Editor synchronization with Properties and Outline View
	 */
	private void checkVPEEditorSynchronization(){
	  
    final SWTBotEclipseEditor jspTextEditor = bot.editorByTitle(TEST_PAGE).toTextEditor();

    int lineIndex = 0;
    int messageLineIndex = -1;
    Iterator<String> lineIterator = jspTextEditor.getLines().iterator();
    while (lineIterator.hasNext() && messageLineIndex == -1) {
      if (lineIterator.next().trim().startsWith("<h:messages ")) {
        messageLineIndex = lineIndex;
      } else {
        lineIndex++;
      }
    }

    jspTextEditor.navigateTo(messageLineIndex, 3);
	  
	  KeyboardHelper.pressKeyCode(bot.getDisplay(), SWT.ARROW_LEFT);
	  
	  delay();
	  
	  assertTrue("h:messages node is not selected within Outline View",
	    bot.viewByTitle(WidgetVariables.OUTLINE).bot().tree().selection().get(0).get(0).startsWith("h:messages "));
	  
	  SWTBotTree outlineTree = bot.viewByTitle(WidgetVariables.PROPERTIES).bot().tree();
	  outlineTree.getTreeItem("Attributes").expand().getNode("style").select();
	  
	  assertTrue("style attribute of h:message node has wrong value within Properties view" +
	  		". Should be 'color: red'",
      outlineTree.selection().get(0).get(1).startsWith("color: red"));
	  
	  jspTextEditor.close();
	  
	}
}