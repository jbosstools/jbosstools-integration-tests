package org.jboss.tools.bpmn2.itests.editor.constructs.events;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.Construct;
import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType;

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
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#conditionalEventDefinition(java.lang.String)
	 */
	@Override
	public void conditionalEventDefinition(String condition) {
		super.conditionalEventDefinition(condition);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#messageEventDefinition(java.lang.String, java.lang.String, org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType)
	 */
	@Override
	public void messageEventDefinition(String operationName, String messageName, AbstractDataType dataType) {
		super.messageEventDefinition(operationName, messageName, dataType);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#signalEventDefinition(java.lang.String, org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType)
	 */
	@Override
	public void signalEventDefinition(String signalName, AbstractDataType dataType) {
		super.signalEventDefinition(signalName, dataType);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#timerEventDefinition(java.lang.String)
	 */
	@Override
	public void timerEventDefinition(String duration) {
		super.timerEventDefinition(duration);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#compensateEventDefinition(java.lang.String, boolean)
	 */
	@Override
	public void compensateEventDefinition(String activityName, boolean waitForCompletion) {
		super.compensateEventDefinition(activityName, waitForCompletion);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#errorEventDefinition(java.lang.String, java.lang.String, org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType)
	 */
	@Override
	public void errorEventDefinition(String errorName, String errorCode, AbstractDataType errorDataType) {
		super.errorEventDefinition(errorName, errorCode, errorDataType);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#escalationEventDefinition(java.lang.String, java.lang.String, org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType)
	 */
	@Override
	public void escalationEventDefinition(String escalationName, String escalationCode, AbstractDataType escalationDataType) {
		super.escalationEventDefinition(escalationName, escalationCode, escalationDataType);
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
