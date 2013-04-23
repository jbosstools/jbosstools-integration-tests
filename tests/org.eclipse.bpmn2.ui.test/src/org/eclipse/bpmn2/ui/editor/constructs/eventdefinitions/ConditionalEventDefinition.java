package org.eclipse.bpmn2.ui.editor.constructs.eventdefinitions;

import org.eclipse.bpmn2.ui.editor.constructs.AbstractEventDefinition;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ConditionalEventDefinition extends AbstractEventDefinition {
	
	private String condition;
	
	public ConditionalEventDefinition(String condition) {
		this(condition, 0);
	}
	
	public ConditionalEventDefinition(String condition, int index) {
		super("Conditional Event Definition", index);
		
		this.condition = condition;
	}
	
	@Override
	protected void setUpDefinition() {
		new DefaultCombo("Script Language").setSelection("XPath");
		new LabeledText("Script").setText(condition);
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
}
