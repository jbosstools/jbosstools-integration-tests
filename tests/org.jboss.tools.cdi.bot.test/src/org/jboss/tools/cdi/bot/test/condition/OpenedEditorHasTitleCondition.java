package org.jboss.tools.cdi.bot.test.condition;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

public class OpenedEditorHasTitleCondition implements ICondition {

	private String title;
	private SWTBotExt bot = SWTBotFactory.getBot();
	
	public OpenedEditorHasTitleCondition(String title) {
		this.title = title;
	}
	
	public boolean test() throws Exception {
		for (SWTBotEditor editor : bot.editors()) {
			if (editor.getTitle().equals(title)) {
				return true;
			}
		}
		return false;
	}

	public void init(SWTBot bot) {
		// nothing do here
	}

	public String getFailureMessage() {
		return "No opened editor has title: " + title; 
	}
	
}
