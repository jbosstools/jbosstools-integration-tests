package org.eclipse.bpmn2.ui.editor.constructs.events;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractEvent;
import org.eclipse.bpmn2.ui.editor.properties.datatypes.AbstractDataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class IntermediateCatchEvent extends AbstractEvent {

	/**
	 * 
	 * @param name
	 */
	public IntermediateCatchEvent(String name) {
		super(name, ConstructType.INTERMEDIATE_CATCH_EVENT);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractEvent#conditionalEventDefinition(java.lang.String)
	 */
	@Override
	public void conditionalEventDefinition(String condition) {
		super.conditionalEventDefinition(condition);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractEvent#messageEventDefinition(java.lang.String, java.lang.String, org.eclipse.bpmn2.ui.editor.properties.datatypes.AbstractDataType)
	 */
	@Override
	public void messageEventDefinition(String operationName, String messageName, AbstractDataType dataType) {
		super.messageEventDefinition(operationName, messageName, dataType);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractEvent#signalEventDefinition(java.lang.String, org.eclipse.bpmn2.ui.editor.properties.datatypes.AbstractDataType)
	 */
	@Override
	public void signalEventDefinition(String signalName, AbstractDataType dataType) {
		super.signalEventDefinition(signalName, dataType);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractEvent#timerEventDefinition(java.lang.String)
	 */
	@Override
	public void timerEventDefinition(String duration) {
		super.timerEventDefinition(duration);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractEvent#linkEventDefinition()
	 */
	@Override
	public void linkEventDefinition() {
		super.linkEventDefinition();
	}
	
}
