package org.jboss.tools.bpmn2.itests.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class TimerStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public TimerStartEvent(String name) {
		super(name, ConstructType.TIMER_START_EVENT);
	}

}