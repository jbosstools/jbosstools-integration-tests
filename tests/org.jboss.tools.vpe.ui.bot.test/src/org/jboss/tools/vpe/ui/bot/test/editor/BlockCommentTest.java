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
import org.jboss.tools.ui.bot.ext.SWTJBTExt;

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
		checkVPE("BlockCommentTestToggle.xml"); //$NON-NLS-1$
		
		//Test remove block comment from Source menu
		
		getEditor().selectLine(22);
		bot.menu("Source").menu("Remove Block Comment").click();  //$NON-NLS-1$//$NON-NLS-2$
		getEditor().save();
		checkVPE("CommentTestUntoggle.xml"); //$NON-NLS-1$
	
		
		//Test add block comment with CTRL+SHIFT+/ hot keys
		
		getEditor().selectLine(22);
		pressBlockCommentHotKeys();
		getEditor().save();
		checkVPE("BlockCommentTestToggle.xml"); //$NON-NLS-1$
		
		//Test remove block comment with CTRL+SHIFT+\ hot keys

		getEditor().selectLine(22);
		pressUnBlockCommentHotKeys();
		getEditor().save();
		checkVPE("CommentTestUntoggle.xml"); //$NON-NLS-1$
		
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
