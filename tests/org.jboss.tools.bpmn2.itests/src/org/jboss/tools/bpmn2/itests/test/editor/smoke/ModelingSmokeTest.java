package org.jboss.tools.bpmn2.itests.test.editor.smoke;

import org.jboss.tools.bpmn2.itests.editor.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.itests.editor.ConnectionType;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="Evaluation", project="EmployeeEvaluation")
public class ModelingSmokeTest extends JBPM6BaseTest {

	/**
	 * ISSUES:
	 *  1) changing the BPMN2 runtime requires project close/open.   
	 *  
	 * @throws Exception
	 */
	@Test
	public void smokeTestModeling() throws Exception {
		/*
		 * Modeling
		 */
		StartEvent start = new StartEvent("StartProcess");
		start.select();
		start.setName("Start");
		start.append("Self Evaluation", ConstructType.USER_TASK);
		
		UserTask userTask1 = new UserTask("Self Evaluation");
		userTask1.addActor("" ,"employee"); 
		userTask1.append("Gateway1", ConstructType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW);
		
		ParallelGateway gateway1 = new ParallelGateway("Gateway1");
		gateway1.setDirection(Direction.DIVERGING);
		
		gateway1.append("HR Evaluation", ConstructType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.NORTH_EAST);
		gateway1.append("PM Evaluation", ConstructType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);

		UserTask userTask2 = new UserTask("HR Evaluation");
		userTask2.addActor("", "Mary");
		userTask2.append("Gateway2", ConstructType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);
		
		ParallelGateway gateway2 = new ParallelGateway("Gateway2");
		gateway2.setDirection(Direction.CONVERGING);

		UserTask userTask3 = new UserTask("PM Evaluation");
	    userTask3.addActor("", "John");
		userTask3.connectTo(gateway2);
		
		gateway2.append("End", ConstructType.END_EVENT);
	}
	
}