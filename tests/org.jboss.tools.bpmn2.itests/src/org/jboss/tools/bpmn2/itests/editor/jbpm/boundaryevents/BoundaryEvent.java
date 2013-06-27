package org.jboss.tools.bpmn2.itests.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.itests.editor.AbstractEvent;
import org.jboss.tools.bpmn2.itests.editor.Construct;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BoundaryEvent extends AbstractEvent {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public BoundaryEvent(String name, ConstructType type) {
		super(name, type);
	}

	/**
	 * 
	 * @param construct
	 */
	public void addTo(Construct construct) {
		editor.activateTool("Boundary Event");
		editor.click(construct.getName());
	}
	
}
