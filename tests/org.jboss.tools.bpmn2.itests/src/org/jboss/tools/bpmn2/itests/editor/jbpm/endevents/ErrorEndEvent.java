package org.jboss.tools.bpmn2.itests.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ErrorEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public ErrorEndEvent(String name) {
		super(name, ConstructType.ERROR_END_EVENT);
	}

}
