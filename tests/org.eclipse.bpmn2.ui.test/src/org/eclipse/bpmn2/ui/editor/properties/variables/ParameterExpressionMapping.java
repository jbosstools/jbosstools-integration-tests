package org.eclipse.bpmn2.ui.editor.properties.variables;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author mbaluch
 */
public class ParameterExpressionMapping implements IMapping {

	String language;
	
	String script;
	
	public ParameterExpressionMapping(String script) {
		this(script, "mvel");
	}
	
	public ParameterExpressionMapping(String script, String language) {
		this.script = script;
		this.language = language;
	}
	
	public void add() {
		new RadioButton("Map an Expression").click();
		new DefaultCombo("Script Language").setSelection(language);
		new LabeledText("Script").setText(script);
		
	}

}
