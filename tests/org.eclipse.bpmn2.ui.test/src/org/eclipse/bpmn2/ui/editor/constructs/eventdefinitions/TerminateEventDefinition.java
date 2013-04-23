package org.eclipse.bpmn2.ui.editor.constructs.eventdefinitions;

import org.eclipse.bpmn2.ui.editor.constructs.AbstractEventDefinition;

public class TerminateEventDefinition extends AbstractEventDefinition {

	public TerminateEventDefinition() {
		this(0);
	}
	
	public TerminateEventDefinition(int index) {
		super("Terminate Event Defnition", index);
	}
	
	@Override
	protected void setUpDefinition() {
		// no operation
	}
	
}
