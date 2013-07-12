package org.jboss.tools.jbpm.ui.bot.test.editor;

import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author apodhrad
 * 
 */
public class JBPMEditor extends SWTBotGefEditor {

	public JBPMEditor() {
		super(Bot.get().activeEditor().getReference(), Bot.get());
	}

	public JBPMEditor(String title) {
		super(Bot.get().editorByTitle(title).getReference(), Bot.get());
	}

	public void insertEntity(String title, int x, int y) {
		activateTool(title);
		click(x, y);
	}
}
