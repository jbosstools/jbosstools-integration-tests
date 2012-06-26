package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.core.resources.IProject;

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;

import org.jboss.tools.bpel.ui.bot.ext.widgets.BotBpelEditor;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;


import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.jboss.tools.ui.bot.ext.view.ServersView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Require(clearProjects = true, server = @Server(type = ServerType.ALL, state = ServerState.Present))
public class SimpleModelingTest extends BPELTest {

	static String BUNDLE = "org.jboss.tools.bpel.ui.bot.test";

	IProject project;
	ServersView sView = new ServersView();
	PackageExplorer pExplorer = new PackageExplorer();

	@Before
	public void setupWorkspace() throws Exception {
		pExplorer.deleteAllProjects();
		ResourceHelper.importProject(BUNDLE, "/projects/DiscriminantProcess",
				"DiscriminantProcess");
		
		bot.viewByTitle("Package Explorer").setFocus();
		pExplorer.selectProject("DiscriminantProcess");
	}

	@After
	public void cleanupWorkspace() throws Exception {
//		pExplorer.deleteAllProjects();
	}

	@Test
	public void testActions() throws Exception {
		openFile("DiscriminantProcess", "bpelContent", "Discriminant.bpel");

		SWTGefBot gefBot = new SWTGefBot();
		final SWTBotGefEditor editor = gefBot.gefEditor("Discriminant.bpel");
		final BotBpelEditor bpel = new BotBpelEditor(editor, gefBot);
		bpel.activatePage("Design");

		bpel.addReceive("receive", "DiscriminantRequest", new String[] {"client", "Discriminant", "calculateDiscriminant"}, true);
		bpel.addAssignVarToVar("assignRequest", 
				new String[] {
					"DiscriminantRequest : DiscriminantRequestMessage", 
					"parameters : DiscriminantRequest", 
					"a : decimal"}, 
				new String[] {
					"MathRequest1 : MathRequestMessage", 
					"parameters : MathRequest", 
					"a : decimal"}
	    );
		bpel.addAssignVarToVar("assignRequest", 
				new String[] {
					"DiscriminantRequest : DiscriminantRequestMessage", 
					"parameters : DiscriminantRequest", 
					"b : decimal"}, 
				new String[] {
					"MathRequest1 : MathRequestMessage", 
					"parameters : MathRequest", 
					"b : decimal"}
		);
		bpel.addAssignFixedToVar("assignRequest", "*", 
				new String[] {
					"MathRequest1 : MathRequestMessage", 
					"parameters : MathRequest", 
					"operator : string"}
		);
		bpel.addValidate("validateInput", "DiscriminantRequest");
		bpel.addInvoke("invokePartner", "MathRequest1", "MathResponse1", new String[] {"math", "Math", "calculate"});
		bpel.addAssignVarToVar("assignResponse", 
				new String[] {
					"MathResponse1 : MathResponseMessage", 
					"parameters : MathResponse", 
					"result : decimal"}, 
				new String[] {
					"DiscriminantResponse : DiscriminantResponseMessage", 
					"parameters : DiscriminantResponse", 
					"result : int"}
		);
		bpel.addReply("reply", "DiscriminantResponse", "", new String[] {"client", "Discriminant", "calculateDiscriminant"});
		
		bpel.save();
	}

}
