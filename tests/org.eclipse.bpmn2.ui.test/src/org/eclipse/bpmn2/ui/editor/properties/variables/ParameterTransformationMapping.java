package org.eclipse.bpmn2.ui.editor.properties.variables;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author mbaluch
 */
public class ParameterTransformationMapping implements IMapping {

	String language;
	
	String script;
	
	public ParameterTransformationMapping(String script) {
		this.language = "XPath";
		this.script = script;
	}
	
	public void add() {
		new RadioButton("Map a Transformation").click();
		new DefaultCombo("Script Language").setSelection("XPath");
		new LabeledText("Script").setText(script);
	}
	
}
