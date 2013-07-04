package org.jboss.tools.bpmn2.itests.editor.jbpm.catchevents;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

public class SignalIntermediateCatchEvent extends IntermediateCatchEvent {

	public SignalIntermediateCatchEvent(String name) {
		super(name, ConstructType.SIGNAL_INTERMEDIATE_CATCH_EVENT);
	}
	
	public void setSignal(String signalName) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		
		SWTBotCombo nameBox = bot.comboBoxWithLabel("Signal");
		if (properties.contains(nameBox, signalName)) {
			nameBox.setSelection(signalName);
		} else {
			new PushButton(0).click();
			new LabeledText("Name").setText(signalName);
			new PushButton("OK").click();
		}
		properties.toolbarButton("Signal Event Definition Details", "Close").click();
	}
	
	
}
