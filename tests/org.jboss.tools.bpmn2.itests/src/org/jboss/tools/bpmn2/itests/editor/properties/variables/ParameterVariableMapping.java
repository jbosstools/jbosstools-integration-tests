package org.jboss.tools.bpmn2.itests.editor.properties.variables;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * 
 * @author mbaluch
 */
public class ParameterVariableMapping implements IMapping {

	String variable;
	
	
	public ParameterVariableMapping(String variable) {
		this.variable = variable;
	}
	
	public void add() {
		new RadioButton("Map to a Variable").click();
		new DefaultCombo("Mapped To").setSelection(variable);
		
	}
	
}
