package org.jboss.tools.cdi.bot.test.condition;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

public class OpenedEditorHasTitleCondition implements WaitCondition {

	private String title;
	private SWTBotExt bot = SWTBotFactory.getBot();
	
	public OpenedEditorHasTitleCondition(String title) {
		this.title = title;
	}
	
	public boolean test() {
		for (SWTBotEditor editor : bot.editors()) {
			if (editor.getTitle().equals(title)) {
				return true;
			}
		}
		return false;
	}

	public String description() {
		return "No opened editor has title: " + title;
	}
	
}
