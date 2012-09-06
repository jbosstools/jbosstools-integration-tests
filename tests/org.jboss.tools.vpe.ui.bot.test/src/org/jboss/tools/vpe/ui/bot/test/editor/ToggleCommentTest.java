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

public class ToggleCommentTest extends VPEEditorTestCase{
	
	public void testToggleComment() throws Throwable{
		
		//Test open page
		
		openPage();
		
		setEditor(bot.editorByTitle(TEST_PAGE).toTextEditor());
		setEditorText(getEditor().getText());
	
		//Test toggle comment from Source menu
		getEditor().navigateTo(22,22);
		bot.menu("Source").menu("Toggle Comment").click(); //$NON-NLS-1$ //$NON-NLS-2$
		getEditor().save();
		waitForBlockingJobsAcomplished(VISUAL_UPDATE);
		SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(TEST_PAGE, new SWTBotExt());
		assertVisualEditorContainsManyComments(webBrowser, 1, TEST_PAGE);
		final String commentValue = "<h:commandButton action=\"hello\" value=\"Say Hello!\" />";
		assertTrue("Visual Representation of page doesn't contain comment with value " + commentValue,
		    webBrowser.containsCommentWithValue(commentValue));
		
		//Test untoggle comment from Source menu

		getEditor().navigateTo(22,22);
		bot.menu("Source").menu("Toggle Comment").click(); //$NON-NLS-1$ //$NON-NLS-2$
		getEditor().save();
    waitForBlockingJobsAcomplished(VISUAL_UPDATE);
    assertVisualEditorContainsManyComments(webBrowser, 0, TEST_PAGE);

		//Test toggle comment with CTRL+SHIFT+C hot keys
		
		getEditor().navigateTo(22,22);
		pressToggleCommentHotKeys();
		getEditor().save();
    waitForBlockingJobsAcomplished(VISUAL_UPDATE);
    assertVisualEditorContainsManyComments(webBrowser, 1, TEST_PAGE);
    assertTrue("Visual Representation of page doesn't contain comment with value " + commentValue,
        webBrowser.containsCommentWithValue(commentValue));
		
		//Test untoggle comment with CTRL+SHIFT hot keys

		getEditor().navigateTo(22,22);
		pressToggleCommentHotKeys();
		getEditor().save();
    waitForBlockingJobsAcomplished(VISUAL_UPDATE);
    assertVisualEditorContainsManyComments(webBrowser, 0, TEST_PAGE);
		
	}
	
	private void pressToggleCommentHotKeys(){
	  if (SWTJBTExt.isRunningOnMacOs()){
	    bot.shells()[0].pressShortcut(SWT.SHIFT | SWT.COMMAND,'c');
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
	        event.character = 'c';
	        display.post(event);
	        event = new Event();
	        event.type = SWT.KeyUp;
	        event.character = 'c';
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
