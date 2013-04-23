package org.jboss.tools.bpmn2.itests.editor.constructs.tasks;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ManualTask extends AbstractTask {
	
	/**
	 * 
	 * @param name
	 */
	public ManualTask(String name) {
		super(name, ConstructType.MANUAL_TASK);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter)
	 */
	@Override
	public void addParameterMapping(IParameter parameter) {
		super.addParameterMapping(parameter);
	}

}
