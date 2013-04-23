package org.jboss.tools.bpmn2.itests.editor.constructs.events;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent;
import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class StartEvent extends AbstractEvent {

	/**
	 * 
	 * @param name
	 */
	public StartEvent(String name) {
		super(name, ConstructType.START_EVENT);
	}

	/**
	 * 
	 * @param b
	 */
	public void setInterrupting(boolean b) {
		properties.selectCheckBox(new CheckBox("Is Interrupting"), b);
	}
	
	/**
	 * 
	 * @param b
	 */
	public void setParallelMultiple(boolean b) {
		properties.selectCheckBox(new CheckBox("Parallel Multiple"), b);
	}
	
	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#conditionalEventDefinition(java.lang.String)
	 */
	@Override
	protected void conditionalEventDefinition(String condition) {
		super.conditionalEventDefinition(condition);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#messageEventDefinition(java.lang.String, java.lang.String, org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType)
	 */
	@Override
	protected void messageEventDefinition(String operationName, String messageName, AbstractDataType dataType) {
		super.messageEventDefinition(operationName, messageName, dataType);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#signalEventDefinition(java.lang.String, org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType)
	 */
	@Override
	protected void signalEventDefinition(String signalName, AbstractDataType dataType) {
		super.signalEventDefinition(signalName, dataType);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEvent#timerEventDefinition(java.lang.String)
	 */
	@Override
	protected void timerEventDefinition(String duration) {
		super.timerEventDefinition(duration);
	}

}