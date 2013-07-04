package org.jboss.tools.bpmn2.itests.editor.jbpm.startevents;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ConditionalStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public ConditionalStartEvent(String name) {
		super(name, ConstructType.CONDITIONAL_START_EVENT);
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setCondition(String language, String script) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		
		new DefaultCombo("Script Language").setSelection(language);
		new LabeledText("Script").setText(script);
	}
	
}