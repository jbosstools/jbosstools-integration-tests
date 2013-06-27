package org.jboss.tools.bpmn2.itests.editor.jbpm.activities;

import org.jboss.tools.bpmn2.itests.editor.AbstractTask;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.IParameterMapping;

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
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.itests.editor.IParameterMapping)
	 */
	@Override
	public void addParameterMapping(IParameterMapping parameter) {
		super.addParameterMapping(parameter);
	}

}
