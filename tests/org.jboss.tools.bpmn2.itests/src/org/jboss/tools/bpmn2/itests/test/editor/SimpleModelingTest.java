package org.jboss.tools.bpmn2.itests.test.editor;


import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;

import org.jboss.tools.bpmn2.itests.editor.ConnectionType;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.UserTask;
import org.jboss.tools.bpmn2.itests.test.Activator;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;

import org.junit.Before;
import org.junit.BeforeClass;

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
			String projectLocation = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, 
					"resources/projects/" + PROJECT_NAME + ".zip");
			ImportHelper.importProjectFromZip(projectLocation);
		}
	}
	
	@Before
	public void openFile() {
		eclipse.maximizeActiveShell();
		projectExplorer.openFile("EditorTestProject", "Evaluation.bpmn");
	}
	
	public void testSmokeModeling() throws Exception {
		StartEvent start = new StartEvent("Start");
		start.append("Self Evaluation", ConstructType.USER_TASK);
		
		UserTask userTask1 = new UserTask("Self Evaluation");
		userTask1.append("Gateway1", ConstructType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW, Position.NORTH_WEST);
		
//		ParallelGateway gateway1 = new ParallelGateway("Gateway1");
//		gateway1.setDirection(Direction.DIVERGING);
//		
//		gateway1.append("HR Evaluation", ConstructType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.NORTH_EAST);
//		gateway1.append("PM Evaluation", ConstructType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.SOUTH);
//		
//		UserTask userTask2 = new UserTask("HR Evaluation");
//		userTask2.append("Gateway2", ConstructType.PARALLEL_GATEWAY);
//		
//		ParallelGateway gateway2 = new ParallelGateway("Gateway2");
//		gateway2.setDirection(Direction.CONVERGING);
//		
//		UserTask userTask3 = new UserTask("PM Evaluation");
//		userTask3.connectTo(gateway2);
//		
//		gateway2.append("End", ConstructType.END_EVENT);
		
		Thread.sleep(10000);
	}
	
}