package org.jboss.tools.bpmn2.itests.test.editor.smoke;

import junit.framework.Assert;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;

import org.jboss.tools.bpmn2.itests.editor.BPMN2Editor;
import org.jboss.tools.bpmn2.itests.editor.ConnectionType;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.UserTask;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.validator.BPMN2Validator;

import org.junit.Test;

/**
 * ISSUES:
 * 	1) setting target namespace to "" on the process will keep the old
 *     value
 *     
 * @author mbaluch
 *
 */
@ProcessDefinition(name="Evaluation", file="EvaluationProcess.bpmn", project="EmployeeEvaluation")
public class ModelingSmokeTest extends SWTBotTestCase {

	/**
	 * ISSUES:
	 * 	1) empty actor language is valid but cannot be chosen after mvel or java was chosen.
	 *  2) cannot edit actor, only recreate.
	 *  3) moving a construct using mouse resets the perspective. So does save (ctrl+s)
	 *  	- change the placement of an activity using d'n'd
	 *  
	 * @throws Exception
	 */
	@Test
	public void smokeTestModeling() throws Exception {
		/*
		 * Modeling
		 */
		StartEvent start = new StartEvent("StartProcess");
		start.setName("Start");
		start.append("Self Evaluation", ConstructType.USER_TASK);
		
		UserTask userTask1 = new UserTask("Self Evaluation");
		userTask1.addActor("employee" ,""); 
		userTask1.append("Gateway1", ConstructType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW);
		
		ParallelGateway gateway1 = new ParallelGateway("Gateway1");
		gateway1.setDirection(Direction.DIVERGING);
		
		gateway1.append("HR Evaluation", ConstructType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.NORTH_EAST);
		gateway1.append("PM Evaluation", ConstructType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);

		UserTask userTask2 = new UserTask("HR Evaluation");
		userTask2.addActor("Mary", "");
		userTask2.append("Gateway2", ConstructType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);
		
		ParallelGateway gateway2 = new ParallelGateway("Gateway2");
		gateway2.setDirection(Direction.CONVERGING);

		UserTask userTask3 = new UserTask("PM Evaluation");
	    userTask3.addActor("John", "");
		userTask3.connectTo(gateway2);
		
		gateway2.append("End", ConstructType.END_EVENT);
		
		new BPMN2Editor().save();
		
		/*
		 * Validation
		 */
		BPMN2Editor editor = new BPMN2Editor();
		BPMN2Validator validator = new BPMN2Validator();
		boolean valid = validator.validate(editor.getSourceText());
		
		Assert.assertTrue(valid);
	}
	
}