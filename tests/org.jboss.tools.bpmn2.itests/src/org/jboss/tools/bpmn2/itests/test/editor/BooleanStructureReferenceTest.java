package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.FromDataOutput;
import org.jboss.tools.bpmn2.itests.editor.jbpm.OutputParameterMapping;
import org.jboss.tools.bpmn2.itests.editor.jbpm.ToVariable;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-BooleanStructureRef", project="EditorTestProject")
public class BooleanStructureReferenceTest extends JBPM6BaseTest {

	@Test
	public void runTest() throws Exception {
		BPMN2Process process = new BPMN2Process("BPMN2-BooleanStructureRef");
		process.addDataType("Boolean");
		process.addLocalVariable("test", "Boolean");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("User Task", ConstructType.USER_TASK);

		UserTask userTask = new UserTask("User Task");
		userTask.addParameterMapping(new OutputParameterMapping(new FromDataOutput("testHT"), new ToVariable("BPMN2-BooleanStructureRef/test")));
		userTask.append("Script", ConstructType.SCRIPT_TASK);

		ScriptTask scriptTask = new ScriptTask("Script");
		scriptTask.setScript("", "System.out.println(\"Result \" + test)");
		scriptTask.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}