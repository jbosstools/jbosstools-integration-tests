/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;

public class BlockCommentTest extends VPEEditorTestCase{

	public void testBlockComment() throws Throwable{
		
		//Test open page
		
		openPage();
		
		setEditor(bot.editorByTitle(TEST_PAGE).toTextEditor());
		setEditorText(getEditor().getText());
	
		//Test add block comment from Source menu
		
		getEditor().selectLine(22);
		bot.menu("Source").menu("Add Block Comment").click();  //$NON-NLS-1$//$NON-NLS-2$
		getEditor().save();
		waitForBlockingJobsAcomplished(VISUAL_UPDATE);
    SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(TEST_PAGE, new SWTBotExt());
    assertVisualEditorContainsManyComments(webBrowser, 1, TEST_PAGE);
    final String commentValue = "<h:commandButton action=\"hello\" value=\"Say Hello!\" />";
    assertTrue("Visual Representation of page doesn't contain comment with value " + commentValue,
        webBrowser.containsCommentWithValue(commentValue));
		
		//Test remove block comment from Source menu
		
		getEditor().selectLine(22);
		bot.menu("Source").menu("Remove Block Comment").click();  //$NON-NLS-1$//$NON-NLS-2$
		getEditor().save();
		waitForBlockingJobsAcomplished(VISUAL_UPDATE);
    assertVisualEditorContainsManyComments(webBrowser, 0, TEST_PAGE);
	
		
		//Test add block comment with CTRL+SHIFT+/ hot keys
		
		getEditor().selectLine(22);
		pressBlockCommentHotKeys();
		getEditor().save();
		waitForBlockingJobsAcomplished(VISUAL_UPDATE);
    assertVisualEditorContainsManyComments(webBrowser, 1, TEST_PAGE);
    assertTrue("Visual Representation of page doesn't contain comment with value " + commentValue,
        webBrowser.containsCommentWithValue(commentValue));
		
		//Test remove block comment with CTRL+SHIFT+\ hot keys

		getEditor().selectLine(22);
		pressUnBlockCommentHotKeys();
		getEditor().save();
		waitForBlockingJobsAcomplished(VISUAL_UPDATE);
    assertVisualEditorContainsManyComments(webBrowser, 0, TEST_PAGE);
		
	}
	
	private void pressBlockCommentHotKeys(){
    if (SWTJBTExt.isRunningOnMacOs()) {
      bot.shells()[0].pressShortcut(SWT.COMMAND, '/');
    } else {
      bot.getDisplay().syncExec(new Runnable() {
        public void run() {
          Display display = bot.getDisplay();
          Event event = new Event();
          event.type = SWT.KeyDown;
          event.keyCode = SWT.CTRL;
          display.post(event);
          event = new Event();
          event.type = SWT.KeyDown;
          event.keyCode = SWT.SHIFT;
          display.post(event);
          event = new Event();
          event.type = SWT.KeyDown;
          event.character = '/';
          display.post(event);
          event = new Event();
          event.type = SWT.KeyUp;
          event.character = '/';
          display.post(event);
          event = new Event();
          event.type = SWT.KeyUp;
          event.keyCode = SWT.SHIFT;
          display.post(event);
          event = new Event();
          event.type = SWT.KeyUp;
          event.keyCode = SWT.CTRL;
          display.post(event);
        }
      });
    }
	}

	private void pressUnBlockCommentHotKeys(){
	  if (SWTJBTExt.isRunningOnMacOs()){
      bot.shells()[0].pressShortcut(SWT.COMMAND,'\\');
    }
    else{
      bot.getDisplay().syncExec(new Runnable() {
        public void run() {
          Display display = bot.getDisplay();
          Event event = new Event();
          event.type = SWT.KeyDown;
          event.keyCode = SWT.CTRL;
          display.post(event);
          event = new Event();
          event.type = SWT.KeyDown;
          event.keyCode = SWT.SHIFT;
          display.post(event);
          event = new Event();
          event.type = SWT.KeyDown;
          event.character = '\\';
          display.post(event);
          event = new Event();
          event.type = SWT.KeyUp;
          event.character = '\\';
          display.post(event);
          event = new Event();
          event.type = SWT.KeyUp;
          event.keyCode = SWT.SHIFT;
          display.post(event);
          event = new Event();
          event.type = SWT.KeyUp;
          event.keyCode = SWT.CTRL;
          display.post(event);
        }
      });  
    }
  }
	
}
