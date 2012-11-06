package org.jboss.tools.bpel.ui.bot.ext.editor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.ui.IEditorReference;

/**
 * 
 * @author apodhrad
 *
 */
public class BpelDescriptorEditor extends SWTBotEditor {

	public BpelDescriptorEditor(IEditorReference editorReference, SWTWorkbenchBot bot)
			throws WidgetNotFoundException {
		super(editorReference, bot);
	}
	
	

}
