package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.throwevents.EscalationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-IntermediateThrowEscalationEvent", project="EditorTestProject")
public class IntermediateThrowEscalationEventTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Escalation Event", ConstructType.ESCALATION_INTERMEDIATE_THROW_EVENT);

		EscalationIntermediateThrowEvent ithrow = new EscalationIntermediateThrowEvent("Escalation Event");
		ithrow.setEscalation("", "MyEscalation");
		ithrow.append("EndProcess", ConstructType.END_EVENT);
	}
	
}