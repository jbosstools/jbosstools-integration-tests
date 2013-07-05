package org.jboss.tools.bpmn2.itests.editor.jbpm.catchevents;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

public class TimerIntermediateCatchEvent extends IntermediateCatchEvent {

	public enum Type {
		DURATION, INTERVAL
	}
	
	public TimerIntermediateCatchEvent(String name) {
		super(name, ConstructType.TIMER_INTERMEDIATE_CATCH_EVENT);
	}
	
	public void setTimer(String value) {
		setTimer(value, Type.DURATION);
	}
	
	public void setTimer(String value, Type type) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		
		switch (type) {
			case INTERVAL:
				new RadioButton("Interval").click();
				break;
			case DURATION:
				new RadioButton("Duration").click();
				break;
			default:
				throw new UnsupportedOperationException();
		}
		
		new LabeledText("Value").setText(value);
		
		properties.toolbarButton("Timer Event Definition Details", "Close").click();
	}
	
}
