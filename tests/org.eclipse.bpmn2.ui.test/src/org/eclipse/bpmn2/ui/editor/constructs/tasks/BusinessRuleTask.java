package org.eclipse.bpmn2.ui.editor.constructs.tasks;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractTask;
import org.jboss.reddeer.swt.impl.text.LabeledText;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BusinessRuleTask extends AbstractTask {
	
	/**
	 * 
	 * @param name
	 */
	public BusinessRuleTask(String name) {
		super(name, ConstructType.BUSINESS_RULE_TASK);
	}

	public void setRuleFlowGroup(String group) {
		properties.selectTab("Business Rule Task");
		new LabeledText("Rule Flow Group").setText(group);
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
	
}
