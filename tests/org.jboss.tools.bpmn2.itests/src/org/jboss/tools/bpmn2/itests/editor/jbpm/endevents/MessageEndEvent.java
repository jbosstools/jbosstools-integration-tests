package org.jboss.tools.bpmn2.itests.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class MessageEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public MessageEndEvent(String name) {
		super(name, ConstructType.MESSAGE_END_EVENT);
	}

}
