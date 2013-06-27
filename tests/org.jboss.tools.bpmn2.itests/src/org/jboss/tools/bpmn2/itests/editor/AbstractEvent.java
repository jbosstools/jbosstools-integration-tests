package org.jboss.tools.bpmn2.itests.editor;

import org.jboss.tools.bpmn2.itests.editor.jbpm.Variable;

/**
 * ISSUE: Adding signals even though Cancel was clicked (ESC pressed)
 * 
 * TBD: 
 * 	1) should we not rather use "Information" structure type?
 * 	2) Add Kind and XSD type choosing.
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
	public void addParameterMapping(IParameterMapping mapping) {
		properties.selectTab("Event");
		mapping.add();
	}
	
	/**
	 * 
	 * @param mapping
	 */
	protected void removeParameterMapping(IParameterMapping mapping) {
		properties.selectTab("Event");
		mapping.remove();
	}
	
	// -------------------------------- START
	
	/**
	 * 
	 * @param name
	 * @param dataState
	 * @param dataType
	 */
	protected void addVariable(IVariable variable) {
		properties.selectTab("Event");
		variable.add();
	}
	
	/**
	 * 
	 * @param name
	 */
	protected void removeVariable(String name) {
		properties.selectTab("Event");
		new Variable(name).remove();
	}
	
}
