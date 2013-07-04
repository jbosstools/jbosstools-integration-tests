package org.jboss.tools.bpmn2.itests.editor.jbpm;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.IMappingSide;

/**
 * Mapping from a variable.
 * 
 * @author mbaluch
 */
public class FromExpression implements IMappingSide {

	private String scriptLanguage;
	
	private String script;
	
	/**
	 * Creates a new instance of FromVariable.
	 * 
	 * @param variableName
	 */
	public FromExpression(String scriptLanguage, String script) {
		this.scriptLanguage = scriptLanguage;
		this.script = script;
	}
	
	@Override
	public void add() {
		new RadioButton("Expression").click();
		new DefaultCombo("Script Language").setSelection(scriptLanguage);
		new LabeledText("Script").setText(script);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return script;
	}
	
}
