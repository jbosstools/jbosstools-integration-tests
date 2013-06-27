package org.jboss.tools.bpmn2.itests.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class EscalationEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public EscalationEndEvent(String name) {
		super(name, ConstructType.ESCALATION_END_EVENT);
	}

}
