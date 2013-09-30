package org.jboss.tools.bpmn2.itests.editor.jbpm.endevents;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ErrorEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public ErrorEndEvent(String name) {
		super(name, ConstructType.ERROR_END_EVENT);
	}

	public void setErrorEvent(String errorName, String errorCode, String errorStructure) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();

		/*
		 * TODO: rewrite the code as Reddeer as contains method becomes available.
		 */
		if (properties.contains(bot.comboBoxWithLabel("Error"), errorName)) {
			new DefaultCombo("Error").setSelection(errorName);
		} else {
			// click the add button
			new PushButton(0).click();

			bot.shell("Create New Error").activate();
			if (errorName != null)
				new LabeledText("Name").setText(errorName);
			if (errorCode != null)
				new LabeledText("Error Code").setText(errorCode);
			if (errorStructure != null && !errorStructure.isEmpty()) {
				/*
				 * TODO: rewrite the code as Reddeer as contains method becomes available.
				 */
				if (properties.contains(bot.activeShell().bot().comboBoxWithLabel("Structure"), errorStructure)) {
					new DefaultCombo("Structure").setSelection(errorStructure);
				} else {
					bot.activeShell().bot().button(0).click();
					new LabeledText("Data Type").setText(errorStructure);
					new PushButton("OK").click();
				}
			}
			new PushButton("OK").click();
		}
		
		properties.toolbarButton("Error Event Definition Details", "Close").click();
	}
	
}
