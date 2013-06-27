package org.jboss.tools.bpmn2.itests.editor.jbpm.activities;

import org.jboss.tools.bpmn2.itests.editor.AbstractTask;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.IParameterMapping;

public class Task extends AbstractTask {

	public Task(String name) {
		super(name, ConstructType.TASK);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.itests.editor.IParameterMapping)
	 */
	@Override
	public void addParameterMapping(IParameterMapping parameter) {
		super.addParameterMapping(parameter);
	}

}
