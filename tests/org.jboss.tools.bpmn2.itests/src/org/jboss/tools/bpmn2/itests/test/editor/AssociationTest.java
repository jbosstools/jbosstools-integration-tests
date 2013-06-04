package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConnectionType;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.JBPM5Process;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.TerminateEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.EndEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.other.CallActivity;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.JBPM5OutputParameter;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.JBPM5Parameter;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.ParameterVariableMapping;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;

import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-Association", project="EditorTestProject")
public class AssociationTest extends JBPM6BaseTest {

	@Test
	public void runTest() throws Exception {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Log", ConstructType.SCRIPT_TASK);
		
		ScriptTask script = new ScriptTask("Log");
		script.setScript("", "System.out.println(\"Just outputting something\");");
		
		start.connectTo(script, ConnectionType.ASSOCIATION_UNDIRECTED);
		
		script.append("EndProcess", ConstructType.END_EVENT);
	}
	
}