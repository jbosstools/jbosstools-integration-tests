package org.jboss.tools.jbpm.ui.bot.test.editor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class JBPMEditor extends SWTBotGefEditor {
	
	protected static SWTWorkbenchBot bot = new SWTWorkbenchBot();

	public JBPMEditor() {
		super(bot.activeEditor().getReference(), bot);
	}

	public JBPMEditor(String title) {
		super(bot.editorByTitle(title).getReference(), bot);
	}

	public void insertEntity(String title, int x, int y) {
		activateTool(title);
		click(x, y);
	}
}
