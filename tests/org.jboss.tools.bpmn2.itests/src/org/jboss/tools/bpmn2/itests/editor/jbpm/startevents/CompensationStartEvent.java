package org.jboss.tools.bpmn2.itests.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class CompensationStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public CompensationStartEvent(String name) {
		super(name, ConstructType.COMPENSATION_START_EVENT);
	}
	
}