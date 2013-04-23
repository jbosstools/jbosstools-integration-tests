package org.eclipse.bpmn2.ui.editor.constructs.tasks;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractTask;
import org.eclipse.bpmn2.ui.editor.properties.variables.IParameter;

public class Task extends AbstractTask {

	public Task(String name) {
		super(name, ConstructType.TASK);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#addInputParameter(org.eclipse.bpmn2.ui.editor.properties.variables.IParameter)
	 */
	@Override
	public void addParameterMapping(IParameter parameter) {
		super.addParameterMapping(parameter);
	}

}
