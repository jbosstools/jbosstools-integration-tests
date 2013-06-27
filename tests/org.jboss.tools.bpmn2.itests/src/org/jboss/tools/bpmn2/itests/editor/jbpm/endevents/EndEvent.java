package org.jboss.tools.bpmn2.itests.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.itests.editor.AbstractEvent;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class EndEvent extends AbstractEvent {
	
	/**
	 * 
	 * @param name
	 */
	public EndEvent(String name) {
		super(name, ConstructType.END_EVENT);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
	EndEvent(String name, ConstructType type) {
		super(name, type);
	}
	
}
