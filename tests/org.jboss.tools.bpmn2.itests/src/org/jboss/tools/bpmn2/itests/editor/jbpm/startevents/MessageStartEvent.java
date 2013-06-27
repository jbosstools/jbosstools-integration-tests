package org.jboss.tools.bpmn2.itests.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class MessageStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public MessageStartEvent(String name) {
		super(name, ConstructType.MESSAGE_START_EVENT);
	}

}