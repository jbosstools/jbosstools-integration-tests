package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.TimerStartEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.TimerStartEvent.Type;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-MultipleStartEvent", project="EditorTestProject")
public class MultipleStartEventTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		StartEvent start = new StartEvent("StartProcess");
		start.append("StartTimer", ConstructType.TIMER_START_EVENT, Position.SOUTH);
		start.append("Split", ConstructType.EXCLUSIVE_GATEWAY, Position.SOUTH_EAST);

		ExclusiveGateway gateway = new ExclusiveGateway("Split");
		gateway.setDirection(Direction.CONVERGING);
		gateway.append("User Task", ConstructType.USER_TASK);
		
		TimerStartEvent start2 = new TimerStartEvent("StartTimer");
		start2.setTimer("500", Type.INTERVAL);
		start2.connectTo(gateway);
		
		UserTask task = new UserTask("User Task");
		task.addActor("", "john");
		task.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}