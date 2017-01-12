package org.jboss.tools.runtime.as.ui.bot.test.reddeer.util;

import org.jboss.ide.eclipse.as.reddeer.server.editor.AbstractEditorWithBrowser;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;

/**
 * Waits until the editor with browser contains the specified text. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class EditorWithBrowserContainsTextCondition extends AbstractWaitCondition {

	private String text;
	
	private String actualText;

	private AbstractEditorWithBrowser editor;

	public EditorWithBrowserContainsTextCondition(AbstractEditorWithBrowser editor, String text) {
		this.text = text;
		this.editor = editor;
	}

	@Override
	public boolean test() {
		actualText = editor.getText();
		return actualText.contains(text);
	}

	@Override
	public String description() {
		return "Page should contain text: " + text + ", but contains: " + actualText;
	}
}
