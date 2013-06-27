package org.jboss.tools.bpmn2.itests.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class SignalEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public SignalEndEvent(String name) {
		super(name, ConstructType.SIGNAL_END_EVENT);
	}

}
