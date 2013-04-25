package org.jboss.tools.bpmn2.itests.test.editor;

import junit.framework.Assert;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;

import org.jboss.tools.bpmn2.itests.editor.BPMN2Editor;
import org.jboss.tools.bpmn2.itests.editor.ConnectionType;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.UserTask;
import org.jboss.tools.bpmn2.itests.test.Activator;
import org.jboss.tools.bpmn2.itests.validator.SchemaValidator;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ISSUES:
 * 	1) setting target namespace to "" on the process will keep the old
 *     value
 *     
 * @author mbaluch
 *
 */
//@Require(jbpm = @JBPM(), runOnce = true)
public class SimpleModelingTest extends SWTTestExt {

	public static final String PROJECT_NAME = "EditorTestProject";
	public static final String PROCESS_NAME = "EmptyProcess.bpmn";

	@BeforeClass
	public static void importProject() {
		if (!new PackageExplorer().containsProject(PROJECT_NAME)) {
			String projectLocation = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/projects/" + PROJECT_NAME + ".zip");
			ImportHelper.importProjectFromZip(projectLocation);
		}
	}
	
	@Before
	public void openFile() {
		eclipse.maximizeActiveShell();
		projectExplorer.openFile("EditorTestProject", "Evaluation.bpmn");
	}
	
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
		StartEvent start = new StartEvent("Start");
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
		
		/*
		 * Validation
		 */
		BPMN2Editor editor = new BPMN2Editor();
		SchemaValidator validator = new SchemaValidator();
		boolean valid = validator.validate(editor.getSourceText());
		
		Assert.assertTrue(valid);
	}
	
}