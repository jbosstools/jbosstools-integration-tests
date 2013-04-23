package org.jboss.tools.bpmn2.itests.editor.constructs.tasks;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter;

public class Task extends AbstractTask {

	public Task(String name) {
		super(name, ConstructType.TASK);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter)
	 */
	@Override
	public void addParameterMapping(IParameter parameter) {
		super.addParameterMapping(parameter);
	}

}
