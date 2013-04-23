package org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEventDefinition;

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
