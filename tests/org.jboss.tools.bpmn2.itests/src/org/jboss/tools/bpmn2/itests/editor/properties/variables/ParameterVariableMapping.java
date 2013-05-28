package org.jboss.tools.bpmn2.itests.editor.properties.variables;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
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
		try {
			new RadioButton("Map to a Variable").click();
		} catch (WidgetNotFoundException e) {
			// widget not found
		}
		new DefaultCombo("Mapped To").setSelection(variable);
		
	}
	
}
