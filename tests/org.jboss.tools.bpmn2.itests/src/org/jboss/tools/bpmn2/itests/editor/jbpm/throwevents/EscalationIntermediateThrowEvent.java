package org.jboss.tools.bpmn2.itests.editor.jbpm.throwevents;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.bpmn2.itests.editor.AbstractEvent;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class EscalationIntermediateThrowEvent extends AbstractEvent {
	
	/**
	 * 
	 * @param name
	 */
	public EscalationIntermediateThrowEvent(String name) {
		super(name, ConstructType.ESCALATION_INTERMEDIATE_THROW_EVENT);
	}

	/**
	 * 
	 * @param messageName
	 * @param dataType
	 */
	public void setEscalation(String escalationName, String escalationCode) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		
		SWTBotCombo nameBox = bot.comboBoxWithLabel("Escalation");
		if (properties.contains(nameBox, escalationName)) {
			nameBox.setSelection(escalationName);
		} else {
			/*
			 * Click Add
			 */
			new PushButton(0).click();
			
			SWTBot windowBot = bot.activeShell().bot();
			if (escalationName != null && !escalationName.isEmpty()) {
				windowBot.textWithLabel("Name").setText(escalationName);
			}
			windowBot.textWithLabel("Escalation Code").setText(escalationCode);
			
			new PushButton("OK").click();
		}
		
		properties.toolbarButton("Escalation Event Definition Details", "Close").click();
	}
	
}
