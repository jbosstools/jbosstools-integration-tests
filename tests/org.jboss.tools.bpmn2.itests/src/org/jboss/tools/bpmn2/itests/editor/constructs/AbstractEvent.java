package org.jboss.tools.bpmn2.itests.editor.constructs;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.CompensateEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.ConditionalEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.ErrorEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.EscalationEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.LinkEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.MessageEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.SignalEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.TerminateEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.TimerEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.properties.BPMN2PropertiesView;
import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.AbstractVariable;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.Parameter;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.Variable;

/**
 * TBD: own method for Structure/Data Type creation
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public abstract class AbstractEvent extends Construct {

	/**
	 * Creates a new instance of Event.
	 * 
	 * @param name
	 * @param type
	 * @param parent
	 * @param index
	 */
	public AbstractEvent(String name, ConstructType type) {
		super(name, type);
	}

	/**
	 * 
	 * @param name
	 * @param dataType
	 * @param mapping
	 */
	public void addParameterMapping(IParameter parameter) {
		properties.selectTab("Event");
		parameter.add();
	}
	
	/**
	 * 
	 * @param name
	 */
	protected void removeParameterMapping(String name) {
		properties.selectTab("Event");
		new Parameter(name).remove();
	}
	
	// -------------------------------- START
	
	/**
	 * 
	 * @param name
	 * @param dataState
	 * @param dataType
	 */
	protected void addVariable(AbstractVariable variable) {
		properties.selectTab("Event");
		variable.add();
	}
	
	/**
	 * 
	 * @param name
	 */
	protected void removeVariable(String name) {
		properties.selectTab("Event");
		new Variable(name, null, null).remove();
	}
	
	/**
	 * 
	 * @param event
	 */
	public void addEventDefinition(AbstractEventDefinition eventDefinition) {
		properties.selectTab("Event");
		eventDefinition.add(this);
	}
	
	/**
	 * 
	 * @param condition
	 */         
	protected void conditionalEventDefinition(String condition) {
		addEventDefinition(new ConditionalEventDefinition(condition));
	}
	
	/**
	 * ISSUES:
	 * 	1) Data Type is named data type but in signalEventDefinition it's called Structure!
	 * TBD:
	 * 	1) Setup operation
	 *  2) Add Kind and XSD type choosing.
	 * 
	 * @param operationName
	 * @param messageName
	 * @param messageDataType
	 */
	protected void messageEventDefinition(String operationName, String messageName, AbstractDataType messageDataType) {
		addEventDefinition(new MessageEventDefinition(operationName, messageName, messageDataType));
	}
	
	/**
	 * ISSUE: Adding signals even though Cancel was clicked (ESC pressed)
	 * 
	 * TBD: 
	 * 	1) should we not rather use "Information" structure type?
	 * 	2) Add Kind and XSD type choosing.
	 * 
	 * @param signalName
	 * @param signalDataType
	 */
	protected void signalEventDefinition(String signalName, AbstractDataType signalDataType) {
		addEventDefinition(new SignalEventDefinition(signalName, signalDataType));
	}
	
	/**
	 * 
	 * @param timerDuration
	 */
	protected void timerEventDefinition(String timerDuration) {
		addEventDefinition(new TimerEventDefinition(timerDuration));
	}

	// -------------------------------- END

	/**
	 * 
	 * @param activityName
	 * @param waitForCompletion
	 */
	protected void compensateEventDefinition(String activityName, boolean waitForCompletion) {
		addEventDefinition(new CompensateEventDefinition(activityName, waitForCompletion));
	}
	
	/**
	 * TBD:
	 * 	1) Item Kind, Is Collection
	 * 
	 * @param errorName
	 * @param errorCode
	 * @param errorDataType
	 */
	protected void errorEventDefinition(String errorName, String errorCode, AbstractDataType errorDataType) {
		addEventDefinition(new ErrorEventDefinition(errorName, errorCode, errorDataType));
	}

	/**
	 * 
	 * @param escalationName
	 * @param escalationCode
	 * @param escalationDataType
	 */
	protected void escalationEventDefinition(String escalationName, String escalationCode, AbstractDataType escalationDataType) {
		addEventDefinition(new EscalationEventDefinition(escalationName, escalationCode, escalationDataType));
	}
	
	/**
	 *
	 */
	protected void terminateEventDefinition() {
		addEventDefinition(new TerminateEventDefinition());
	}
	
	// -------------------------------- THROW + all END (except for escalationEventDefinition), 
	
	/**
	 * 
	 */
	protected void linkEventDefinition() {
		addEventDefinition(new LinkEventDefinition("TBD", "TBD", "TBD"));
	}
	
	/**
	 * 
	 * @param eventDefinition
	 */
	public void removeEventDefinition(String eventDefinition) {
		new AbstractEventDefinition(name) {
			
			@Override
			protected void setUpDefinition() {
				// TODO Auto-generated method stub
				
			}
			
		}.remove(this);
	}
	
	/**
	 * 
	 * @return
	 */
	public BPMN2PropertiesView getProperties() {
		return properties;
	}
	
}
