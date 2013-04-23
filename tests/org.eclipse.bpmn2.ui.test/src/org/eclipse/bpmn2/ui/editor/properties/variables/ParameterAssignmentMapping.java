package org.eclipse.bpmn2.ui.editor.properties.variables;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * 
 * @author mbaluch
 */
public class ParameterAssignmentMapping implements IMapping {

	String variableName;
	
	public ParameterAssignmentMapping(String variableName) {
		this.variableName = variableName;
	}
	
	public void add() {
		new RadioButton("Map to a Variable").click();
		new DefaultCombo("Mapped To").setSelection(variableName);
	}
	
}
