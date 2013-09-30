package org.jboss.tools.bpmn2.itests.editor.jbpm.endevents;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class EscalationEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public EscalationEndEvent(String name) {
		super(name, ConstructType.ESCALATION_END_EVENT);
	}

	public void setEscalation(String escalationName, String escalationCode) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		
		SWTBotCombo nameBox = bot.comboBoxWithLabel("Escalation");
		if (properties.contains(nameBox, escalationName)) {
			nameBox.setSelection(escalationName);
		} else {
			new PushButton(0).click();
			new SWTBot().shell("Create New Escalation").activate();
			if (escalationName != null && !escalationName.isEmpty()) {
				new LabeledText("Name").setText(escalationName);
			}
			new LabeledText("Escalation Code").setText(escalationCode);
			new PushButton("OK").click();
		}
		properties.toolbarButton("Escalation Event Definition Details", "Close").click();
	}
	
}
