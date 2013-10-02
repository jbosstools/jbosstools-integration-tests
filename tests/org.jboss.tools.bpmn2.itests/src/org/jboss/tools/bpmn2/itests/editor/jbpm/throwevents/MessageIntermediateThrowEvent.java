package org.jboss.tools.bpmn2.itests.editor.jbpm.throwevents;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.AbstractEvent;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class MessageIntermediateThrowEvent extends AbstractEvent {
	
	/**
	 * 
	 * @param name
	 */
	public MessageIntermediateThrowEvent(String name) {
		super(name, ConstructType.MESSAGE_INTERMEDIATE_THROW_EVENT);
	}

	/**
	 * 
	 * @param messageName
	 * @param dataType
	 */
	public void setMessage(String messageName, String dataType) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		
		SWTBotCombo nameBox = bot.comboBoxWithLabel("Message");
		String messageNameLabel  = messageName + "(" + dataType + ")";
		if (properties.contains(nameBox, messageNameLabel)) {
			nameBox.setSelection(messageNameLabel);
		} else {
			/*
			 * Click Add
			 */
			new PushButton(0).click();
			
			SWTBot windowBot = bot.activeShell().bot();
			windowBot.textWithLabel("Name").setText(messageName);
			
			if (dataType != null && !dataType.isEmpty()) {
				nameBox = windowBot.comboBoxWithLabel("Data Type");
				if (properties.contains(nameBox, dataType)) {
					nameBox.setSelection(dataType);
				} else {
					windowBot.button(0).click();
					new LabeledText("Data Type").setText(dataType);
					new PushButton("OK").click();
				}
			}
			
			new PushButton("OK").click();
			
		}
		
		properties.toolbarButton("Message Event Definition Details", "Close").click();
	}
	
}
