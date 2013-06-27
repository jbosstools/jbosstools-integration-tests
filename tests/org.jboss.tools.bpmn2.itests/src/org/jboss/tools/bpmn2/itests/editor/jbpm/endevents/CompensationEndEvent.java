package org.jboss.tools.bpmn2.itests.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class CompensationEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public CompensationEndEvent(String name) {
		super(name, ConstructType.COMPENSATION_END_EVENT);
	}

}