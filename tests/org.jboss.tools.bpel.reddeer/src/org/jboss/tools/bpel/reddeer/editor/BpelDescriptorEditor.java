package org.jboss.tools.bpel.reddeer.editor;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author apodhrad
 * 
 */
public class BpelDescriptorEditor extends SWTBotEditor {

	public BpelDescriptorEditor() {
		super(Bot.get().activeEditor().getReference(), Bot.get());
	}

	public void setAssociatedPort(String port) {
		Bot.get().table().click(0, 1);
		Bot.get().sleep(1000);
		Bot.get().ccomboBox("-- none -- ").setSelection(port);
		Bot.get().table().click(0, 2);
		save();
	}

}
