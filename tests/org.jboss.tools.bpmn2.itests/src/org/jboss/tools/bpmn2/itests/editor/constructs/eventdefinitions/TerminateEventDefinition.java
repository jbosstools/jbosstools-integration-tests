package org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions;

import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEventDefinition;

public class TerminateEventDefinition extends AbstractEventDefinition {

	public TerminateEventDefinition() {
		this(0);
	}
	
	public TerminateEventDefinition(int index) {
		super("Terminate Event Definition", index);
	}
	
	@Override
	protected void setUpDefinition() {
		// no operation
	}
	
}
