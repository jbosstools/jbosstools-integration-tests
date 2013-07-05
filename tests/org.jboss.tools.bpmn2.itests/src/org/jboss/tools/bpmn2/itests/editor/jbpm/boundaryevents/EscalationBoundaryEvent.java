package org.jboss.tools.bpmn2.itests.editor.jbpm.boundaryevents;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author mbaluch
 */
public class EscalationBoundaryEvent extends BoundaryEvent {

	public EscalationBoundaryEvent(String name) {
		super(name, ConstructType.CONDITIONAL_BOUNDARY_EVENT);
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
			if (escalationName != null && !escalationName.isEmpty())
				new LabeledText("Name").setText(escalationName);
			new LabeledText("Escalation Code").setText(escalationCode);
			new PushButton("OK").click();
		}
		properties.toolbarButton("Escalation Event Definition Details", "Close").click();
	}
	
}
