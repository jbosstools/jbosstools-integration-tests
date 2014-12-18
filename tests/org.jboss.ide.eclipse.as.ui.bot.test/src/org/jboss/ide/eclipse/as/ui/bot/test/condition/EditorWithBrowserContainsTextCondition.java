package org.jboss.ide.eclipse.as.ui.bot.test.condition;

import org.jboss.ide.eclipse.as.reddeer.server.editor.AbstractEditorWithBrowser;
import org.jboss.reddeer.swt.condition.WaitCondition;

/**
 * Waits until server's module web page contains the specified text. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class EditorWithBrowserContainsTextCondition implements WaitCondition {

	private String text;

	private AbstractEditorWithBrowser editor;

	public EditorWithBrowserContainsTextCondition(AbstractEditorWithBrowser editor, String text) {
		this.editor = editor;
		this.text = text;
	}

	@Override
	public boolean test() {
		return editor.getText().contains(text);
	}

	@Override
	public String description() {
		return "Page should contain text: " + text + ", but contains: " + editor.getText();
	}
}
