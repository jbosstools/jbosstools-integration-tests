package org.jboss.tools.bpmn2.itests.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ErrorStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public ErrorStartEvent(String name) {
		super(name, ConstructType.ERROR_START_EVENT);
	}

}