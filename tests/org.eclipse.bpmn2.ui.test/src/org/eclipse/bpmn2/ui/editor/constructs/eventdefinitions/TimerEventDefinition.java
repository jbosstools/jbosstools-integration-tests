package org.eclipse.bpmn2.ui.editor.constructs.eventdefinitions;

import org.eclipse.bpmn2.ui.editor.constructs.AbstractEventDefinition;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class TimerEventDefinition extends AbstractEventDefinition {

	private String timerDuration;
	
	public TimerEventDefinition(String timerDuration) {
		this(timerDuration, 0);
	}
	
	public TimerEventDefinition(String timerDuration, int index) {
		super("Timer Event Definition", index);
		
		this.timerDuration = timerDuration;
	}
	
	@Override
	protected void setUpDefinition() {
		new DefaultTable().select("Timer Event Definition");
		new PushButton("OK").click();
		new RadioButton("Duration").click();
		new LabeledText("Value").setText(timerDuration);
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
}
