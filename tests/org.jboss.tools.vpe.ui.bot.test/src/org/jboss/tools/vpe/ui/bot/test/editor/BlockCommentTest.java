package org.jboss.tools.vpe.ui.bot.test.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

public class BlockCommentTest extends VPEEditorTestCase{

	public void testBlockComment() throws Throwable{
		
		//Test open page
		
		openPage();
		
		setEditor(bot.editorByTitle(TEST_PAGE).toTextEditor());
		setEditorText(getEditor().getText());
	
		//Test add block comment from Source menu
		
		getEditor().navigateTo(22,22);
		bot.menu("Source").menu("Add Block Comment").click();
		checkVPE("BlockCommentTestToggle.xml");
		
		//Test remove block comment from Source menu
		
		getEditor().navigateTo(22,22);
		bot.menu("Source").menu("Remove Block Comment").click();
		checkVPE("CommentTestUntoggle.xml");
	
		
		//Test add block comment with CTRL+SHIFT+/ hot keys
		
		getEditor().navigateTo(22,22);
		pressBlockCommentHotKeys();
		checkVPE("BlockCommentTestToggle.xml");
		
		//Test remove block comment with CTRL+SHIFT+\ hot keys

		getEditor().navigateTo(22,22);
		pressUnBlockCommentHotKeys();
		checkVPE("CommentTestUntoggle.xml");
		
		//Test add block comment lines from Source menu
		
		getEditor().navigateTo(18,22);
		bot.menu("Source").menu("Add Block Comment").click();
		checkVPE("BlockCommentTestLinesToggle.xml");

		//Test remove block comment lines from Source menu
		
		getEditor().navigateTo(18,22);
		bot.menu("Source").menu("Remove Block Comment").click();
		checkVPE("CommentTestUntoggle.xml");
		
		//Test add block comment lines with CTRL+SHIFT+/ hot keys
		
		getEditor().navigateTo(18,22);
		bot.menu("Source").menu("Add Block Comment").click();
		checkVPE("BlockCommentTestLinesToggle.xml");

		//Test remove block comment lines with CTRL+SHIFT+/ hot keys
		
		getEditor().navigateTo(18,22);
		bot.menu("Source").menu("Remove Block Comment").click();
		checkVPE("CommentTestUntoggle.xml");
		
	}
	
	private void pressBlockCommentHotKeys(){
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

	private void pressUnBlockCommentHotKeys(){
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
