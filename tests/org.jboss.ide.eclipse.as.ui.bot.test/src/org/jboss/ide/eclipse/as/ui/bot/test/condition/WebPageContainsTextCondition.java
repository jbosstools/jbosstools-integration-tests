package org.jboss.ide.eclipse.as.ui.bot.test.condition;

import org.jboss.ide.eclipse.as.reddeer.server.editor.ServerModuleWebPageEditor;
import org.jboss.reddeer.swt.condition.WaitCondition;

/**
 * Waits until server's module web page contains the specified text. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class WebPageContainsTextCondition implements WaitCondition {

	private String text;

	private ServerModuleWebPageEditor editor;

	public WebPageContainsTextCondition(ServerModuleWebPageEditor editor, String text) {
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
