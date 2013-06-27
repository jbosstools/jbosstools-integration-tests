package org.jboss.tools.bpmn2.itests.editor.jbpm.throwevents;

import org.jboss.tools.bpmn2.itests.editor.AbstractEvent;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class IntermediateThrowEvent extends AbstractEvent {
	
	/**
	 * 
	 * @param name
	 */
	public IntermediateThrowEvent(String name) {
		super(name, ConstructType.INTERMEDIATE_THROW_EVENT);
	}

}
