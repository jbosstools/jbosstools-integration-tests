package org.eclipse.bpmn2.ui.test.editor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.eclipse.bpmn2.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.BPMN2Process;
import org.eclipse.bpmn2.ui.editor.constructs.JBPM5Process;
import org.eclipse.bpmn2.ui.editor.constructs.events.StartEvent;
import org.eclipse.bpmn2.ui.editor.constructs.tasks.ManualTask;
import org.eclipse.bpmn2.ui.editor.properties.BPMN2PropertiesView;
import org.eclipse.bpmn2.ui.editor.properties.variables.ParameterExpressionMapping;
import org.eclipse.bpmn2.ui.test.Activator;
import org.eclipse.bpmn2.ui.validator.SchemaValidator;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.utils.internal.SiblingFinder;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.util.Bot;

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
public class ConstructTest extends SWTTestExt {

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
		projectExplorer.openFile("EditorTestProject", "EmptyProcess.bpmn");
	}
	
//	@AfterClass()
//	public static void deleteProject() {
//		new PackageExplorer().getProject("TestProject").delete(true);
//	}
	
//	public void testToolbar() throws Exception {
//		BPMN2Editor editor = new BPMN2Editor();
//		editor.selectConstruct("End");
//		
//		BPMN2PropertiesView properties = new BPMN2PropertiesView();
//		properties.selectTab("Event");
//		
//		System.out.println(" --- " + properties.indexOfSection("Input Parameters"));
//
//		Thread.sleep(10000);
//	}
	
	public void testGetSourceText() throws Exception {
		BPMN2Editor editor = new BPMN2Editor();
		
		SchemaValidator validator = new SchemaValidator();
		boolean valid = validator.validate(editor.getSourceText());
		
		System.out.println("Document " + (valid ? "IS" : "IS NOT") + " valid.");
		
		if (!valid) {
			System.out.println("Errors:");
			System.out.println(validator.getErrorList());
			
			System.out.println("Fatal Errors:");
			System.out.println(validator.getFatalErrorList());
		}
		
	}
	
//	public void testAddConstructToProcess() throws Exception {
//		BPMN2Process p = new BPMN2Process();
//		p.add("New Start", ConstructType.START_EVENT);
//		Thread.sleep(TIME_10S);
//	}
	
//	public void testProcessProperties() throws Exception {
//		StartEvent s = new StartEvent("Start");
//		s.select();
//		Thread.sleep(10000);
//		
//		Process p = new Process();
//		p.select();
//		Thread.sleep(10000);
//	}
	
//	public void testDataOutput() {
////		DataOutput doo = new DataOutput("Data Output 1");
//		Message m = new Message("Message 1");
//		m.setDataObjectType("MessageType1");
//	}
	
//	public void testCallActivity() {
//		CallActivity ca = new CallActivity("Call Activity 1");
//		ca.setCalledActivity("XXX");
//	}
	
//	public void testSendTask() {
//		SendTask st = new SendTask("Send Task");
//		st.setMessage("XXX", "XXXType");
//	}
	
//	public void testServiceTask() {
//		ServiceTask st = new ServiceTask("Service Task");
//		st.setOperation("New Operation");
//	}
	
//	public void testBusinessRuleTask() throws Exception {
//		BusinessRuleTask brt = new BusinessRuleTask("Script Task");
//		// TBD
//	}
	
//	public void testScriptTask() throws Exception {
//		ScriptTask st = new ScriptTask("Script Task");
//		st.setScript("Java", "x++");
//	}
	
//	public void testUserTask() throws Exception {
//		UserTask ut = new UserTask("User Task");
//		ut.setName("User Task");
//		ut.setTaskName("Super User Task");
//		ut.setContent("Welcome and work");
//		ut.addActor("xx/yy", "mvel");
//		ut.addInputParameter("par1", "Boolean", new ExpressionMapping("xxx"));
//	}
	
//	public void testManualTask() throws Exception {
//		ManualTask mt = new ManualTask("Manual Task");
//		mt.setOnExistScript("Java", "int i=0;");
//		mt.addOutputParameter("Param2", "Float", new ParameterExpressionMapping("xxx/ttt"));
//	}
	
//	public void testAddInput() throws Exception {
//		Task t = new Task("Task");
//		t.addInputParameter("Param1", "String", new ExpressionMapping("xxx/ttt"));
////		t.addOutputParameter("Param2", "Float", new ExpressionMapping("xxx/ttt"));
//	}
	
//	/**
//	 * 
//	 * @throws Exception
//	 */
//	public void testAddVariableTest() throws Exception {
//		StartEvent start = new StartEvent("Start");
//		start.addVariable("var1", "data1", "dataType1");
//		Thread.sleep(TIME_10S);
//		start.removeVariable("var1");
//		Thread.sleep(TIME_10S);
//	}
	
//	public void testStartEvent() throws Exception {
//		new StartEvent("Start").conditionalEventDefinition("x>10");
//		new StartEvent("Start").timerEventDefinition("10m");
//		new StartEvent("Start").signalEventDefinition("New Signal", "New_Data_Type");
//		new StartEvent("Start").messageEventDefinition("New Signal", "New_Data_Type");
//		
//		new EndEvent("End").compensateEventDefinition("Service Task 1", true);
//		new EndEvent("End").errorEventDefinition("New Error", "500", "NewErrorDataType");
//		new EndEvent("End").escalationEventDefinition("New Escalation", "544", "NewDataType");
//		
//		Thread.sleep(20000);
//	}
	
	/*
	public void testExclusiveGateway() throws Exception {
		StartEvent start = new StartEvent("Start");
		Thread.sleep(5000);
		start.setInterrupting(false);
		start.setParallelMultiple(true);
		Thread.sleep(5000);
	}
	*/
	
//	public void testExclusiveGateway() throws Exception {
//		ExclusiveGateway exclusiveGateway = new ExclusiveGateway("Exclusive");
//		exclusiveGateway.setDirection(Direction.DIVERGING);
//		Thread.sleep(10000);
//		exclusiveGateway.setCondition("Exclusive -> Manual", "x < 0");
//		exclusiveGateway.setCondition("Exclusive -> Script", "x >= 0");
//		Thread.sleep(10000);
//		
//	}
	
//	public void testSimpleModeling() throws Exception {
//		StartEvent start = new StartEvent("Start");
//		start.contextMenuAppend("AdHoc", ConstructType.AD_HOC_SUB_PROCESS);
//		
//		AdHocSubProcess adHocSubProcess = new AdHocSubProcess("AdHoc");
//		adHocSubPro	`cess.append("End", ConstructType.END_EVENT);
//	}
	
}
