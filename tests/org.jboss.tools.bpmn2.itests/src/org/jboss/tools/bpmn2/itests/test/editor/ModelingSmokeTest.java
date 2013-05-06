package org.jboss.tools.bpmn2.itests.test.editor;

import junit.framework.Assert;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.tools.bpmn2.itests.editor.BPMN2Editor;
import org.jboss.tools.bpmn2.itests.editor.ConnectionType;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.UserTask;
import org.jboss.tools.bpmn2.itests.swt.ext.EclipseHelper;
import org.jboss.tools.bpmn2.itests.swt.ext.ImportProjectWizard;
import org.jboss.tools.bpmn2.itests.swt.ext.ResourceHelper;
import org.jboss.tools.bpmn2.itests.swt.ext.JBPM5RuntimeRequirement.JBPM5;
import org.jboss.tools.bpmn2.itests.swt.ext.SetUpWorkspaceRequirement.SetUpWorkspace;
import org.jboss.tools.bpmn2.itests.test.Activator;
import org.jboss.tools.bpmn2.itests.validator.BPMN2Validator;

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
@JBPM5()
@SetUpWorkspace()
public class ModelingSmokeTest extends SWTBotTestCase {

	public static final String PROJECT_NAME = "EditorTestProject";
	public static final String PROCESS_NAME = "EmptyProcess.bpmn";

	@BeforeClass
	public static void importProject() {
		if (!new PackageExplorer().containsProject(PROJECT_NAME)) {
			String projectLocation = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/projects/" + PROJECT_NAME + ".zip");
			new ImportProjectWizard(projectLocation).execute();
		}
	}
	
	@Before
	public void openFile() {
		EclipseHelper.maximizeActiveShell();
		new PackageExplorer().getProject("EditorTestProject").getProjectItem("Evaluation.bpmn").open();
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