package org.jboss.tools.bpmn2.itests.editor.constructs;


import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter;


/**
 * ISSUES:
 * 	1) output parameter doesn't have the possibility to specify mapping type!
 *  2) inconsistent naming. 'java' Vs. 'Java', 'mvel' Vs. 'MVEL' 
 *  	- lower case is present in 'Task' var mapping and upper in 'Manual Task'
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public abstract class AbstractTask extends Construct {

	/**
	 * 
	 * @param name
	 */
	public AbstractTask(String name, ConstructType type) {
		super(name, type);
	}
	
	protected void setIsForCompensation(boolean b) {
		properties.selectTab(type.toName());
		properties.selectCheckBox(Bot.get().checkBoxWithLabel("Is For Compensation"), b);
	}
	
	protected void setOnEntryScript(String language, String script) {
		properties.selectTab(type.toName());
		Bot.get().comboBoxWithLabel("Script Language").setSelection(language);
		Bot.get().textWithLabel("Script").setText(script);
	}
	
	protected void setOnExistScript(String language, String script) {
		properties.selectTab(type.toName());
		Bot.get().comboBoxWithLabel("Script Language", 1).setSelection(language);
		Bot.get().textWithLabel("Script", 1).setText(script);
	}
	
	protected void addParameterMapping(IParameter parameter) {
		properties.selectTab("I/O Parameters");
		parameter.add();
	}
	
}
