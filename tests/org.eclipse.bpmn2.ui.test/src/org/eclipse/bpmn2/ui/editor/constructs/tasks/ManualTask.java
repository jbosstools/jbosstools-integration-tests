package org.eclipse.bpmn2.ui.editor.constructs.tasks;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractTask;
import org.eclipse.bpmn2.ui.editor.properties.variables.IParameter;

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
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#addInputParameter(org.eclipse.bpmn2.ui.editor.properties.variables.IParameter)
	 */
	@Override
	public void addParameterMapping(IParameter parameter) {
		super.addParameterMapping(parameter);
	}

}
