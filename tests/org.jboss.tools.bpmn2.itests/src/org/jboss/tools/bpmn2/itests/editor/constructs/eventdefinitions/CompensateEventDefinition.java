package org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions;

import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEventDefinition;

/**
 * 
 * @author
 */
public class CompensateEventDefinition extends AbstractEventDefinition {

	private String activityName;
	
	private boolean waitForCompletion;
	
	public CompensateEventDefinition(String activityName, boolean waitForCompletion) {
		this(activityName, waitForCompletion, 0);
	}
	
	public CompensateEventDefinition(String activityName, boolean waitForCompletion, int index) {
		super("Compensate Event Definition");
		
		this.activityName = activityName;
		this.waitForCompletion = waitForCompletion;
	}
	
	@Override
	protected void setUpDefinition() {
		bot.comboBox("Activity").setSelection(activityName);
		if (waitForCompletion) bot.checkBox(0).click();
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
}
