package org.jboss.tools.ui.bot.ext.parts;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.ui.IEditorReference;

public class SWTBotEditorExt extends SWTBotEclipseEditor {

	
	public SWTBotEditorExt(IEditorReference editorReference, SWTWorkbenchBot bot)
			throws WidgetNotFoundException {
		super(editorReference, bot);

	}
	
	public ContentAssistBot contentAssist() {
		ContentAssistBot caBot = new ContentAssistBot(this);
		return caBot;
	}
	
}
	
