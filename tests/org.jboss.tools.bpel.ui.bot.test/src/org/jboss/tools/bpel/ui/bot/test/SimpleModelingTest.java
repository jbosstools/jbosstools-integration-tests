package org.jboss.tools.bpel.ui.bot.test;

import org.jboss.tools.bpel.ui.bot.ext.BpelBot;
import org.jboss.tools.bpel.ui.bot.ext.editor.BpelEditor;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.junit.Before;
import org.junit.Test;

@Require(server = @Server(type = ServerType.ALL, state = ServerState.Present), perspective = "BPEL")
public class SimpleModelingTest extends SWTTestExt {

	public static final String PROJECT_NAME = "DiscriminantProcess";
	public static final String BPEL_FILE_NAME = "Discriminant.bpel";
	
	private BpelBot bpelBot;
	
	public SimpleModelingTest() {
		bpelBot = new BpelBot();
	}

	@Before
	public void setupWorkspace() throws Exception {
		ResourceHelper.importProject(Activator.PLUGIN_ID, "/projects/DiscriminantProcess", PROJECT_NAME);
	}

	@Test
	public void testActions() throws Exception {
		BpelEditor bpelEditor = bpelBot.openBpelFile("DiscriminantProcess", "Discriminant.bpel");
		bpelEditor.activateDesignPage();

		bpelEditor.addReceive("receive", "DiscriminantRequest", new String[] {"client", "Discriminant", "calculateDiscriminant"}, true);
		bpelEditor.addAssignVarToVar("assignRequest", 
				new String[] {
					"DiscriminantRequest : DiscriminantRequestMessage", 
					"parameters : DiscriminantRequest", 
					"a : decimal"}, 
				new String[] {
					"MathRequest1 : MathRequestMessage", 
					"parameters : MathRequest", 
					"a : decimal"}
	    );
		bpelEditor.addAssignVarToVar("assignRequest", 
				new String[] {
					"DiscriminantRequest : DiscriminantRequestMessage", 
					"parameters : DiscriminantRequest", 
					"b : decimal"}, 
				new String[] {
					"MathRequest1 : MathRequestMessage", 
					"parameters : MathRequest", 
					"b : decimal"}
		);
		bpelEditor.addAssignFixedToVar("assignRequest", "*", 
				new String[] {
					"MathRequest1 : MathRequestMessage", 
					"parameters : MathRequest", 
					"operator : string"}
		);
		bpelEditor.addValidate("validateInput", "DiscriminantRequest");
		bpelEditor.addInvoke("invokePartner", "MathRequest1", "MathResponse1", new String[] {"math", "Math", "calculate"});
		bpelEditor.addAssignVarToVar("assignResponse", 
				new String[] {
					"MathResponse1 : MathResponseMessage", 
					"parameters : MathResponse", 
					"result : decimal"}, 
				new String[] {
					"DiscriminantResponse : DiscriminantResponseMessage", 
					"parameters : DiscriminantResponse", 
					"result : int"}
		);
		bpelEditor.addReply("reply", "DiscriminantResponse", "", new String[] {"client", "Discriminant", "calculateDiscriminant"});
		
		bpelEditor.save();
	}

}
