package org.jboss.tools.bpel.reddeer.editor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class BpelDescriptorEditor extends SWTBotEditor {
	
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();

	public BpelDescriptorEditor() {
		super(bot.activeEditor().getReference(), bot);
	}

	public void setAssociatedPort(String port) {
		bot.table().click(0, 1);
		bot.sleep(1000);
		bot.ccomboBox("-- none -- ").setSelection(port);
		bot.table().click(0, 2);
		save();
	}

}
