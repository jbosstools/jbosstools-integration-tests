package org.jboss.tools.bpmn2.itests.editor;

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
		properties.selectTab(type.toToolName());
		properties.selectCheckBox(bot.checkBoxWithLabel("Is For Compensation"), b);
	}
	
	protected void setOnEntryScript(String language, String script) {
		properties.selectTab(type.toToolName());
		bot.comboBoxWithLabel("Script Language").setSelection(language);
		bot.textWithLabel("Script").setText(script);
	}
	
	protected void setOnExistScript(String language, String script) {
		properties.selectTab(type.toToolName());
		bot.comboBoxWithLabel("Script Language", 1).setSelection(language);
		bot.textWithLabel("Script", 1).setText(script);
	}
	
	protected void addParameterMapping(IParameterMapping parameter) {
		properties.selectTab("I/O Parameters");
		parameter.add();
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.Construct#addEvent(java.lang.String, org.jboss.tools.bpmn2.itests.editor.ConstructType)
	 */
	@Override
	public void addEvent(String name, ConstructType eventType) {
		super.addEvent(name, eventType);
	}
	
}
