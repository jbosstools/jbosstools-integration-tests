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
		checkVPE("ToggleCommentTestToggle.xml"); //$NON-NLS-1$
		
		//Test untoggle comment from Source menu

		getEditor().navigateTo(22,22);
		bot.menu("Source").menu("Toggle Comment").click(); //$NON-NLS-1$ //$NON-NLS-2$
		getEditor().save();
		checkVPE("CommentTestUntoggle.xml"); //$NON-NLS-1$

		//Test toggle comment with CTRL+SHIFT+C hot keys
		
		getEditor().navigateTo(22,22);
		pressToggleCommentHotKeys();
		getEditor().save();
		checkVPE("ToggleCommentTestToggle.xml"); //$NON-NLS-1$
		
		//Test untoggle comment with CTRL+SHIFT hot keys

		getEditor().navigateTo(22,22);
		pressToggleCommentHotKeys();
		getEditor().save();
		checkVPE("CommentTestUntoggle.xml"); //$NON-NLS-1$
		
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
