package org.jboss.tools.portlet.ui.bot.task.editor;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

/**
 * Closes all editors. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class CloseAllEditors extends AbstractSWTTask {

	@Override
	public void perform() {
		List<? extends SWTBotEditor> editors = SWTBotFactory.getBot().editors();

		for (SWTBotEditor editor : editors){
			editor.close();
		}
	}
}
