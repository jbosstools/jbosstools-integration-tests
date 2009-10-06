package org.jboss.tools.vpe.ui.bot.test.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

public class ToggleCommentTest extends VPEEditorTestCase{
	
	public void testToggleComment() throws Throwable{
		
		//Test open page
		
		openPage();
		
		setEditor(bot.editorByTitle(TEST_PAGE).toTextEditor());
		setEditorText(getEditor().getText());
	
		//Test toggle comment from Source menu
		
		getEditor().navigateTo(22,22);
		bot.menu("Source").menu("Toggle Comment").click();
		checkVPE("ToggleCommentTestToggle.xml");
		
		//Test untoggle comment from Source menu

		getEditor().navigateTo(22,22);
		bot.menu("Source").menu("Toggle Comment").click();
		checkVPE("CommentTestUntoggle.xml");

		//Test toggle comment with CTRL+SHIFT+C hot keys
		
		getEditor().navigateTo(22,22);
		pressToggleCommentHotKeys();
		checkVPE("ToggleCommentTestToggle.xml");
		
		//Test untoggle comment with CTRL+SHIFT hot keys

		getEditor().navigateTo(22,22);
		pressToggleCommentHotKeys();
		checkVPE("CommentTestUntoggle.xml");
		
	}
	
	private void pressToggleCommentHotKeys(){
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
