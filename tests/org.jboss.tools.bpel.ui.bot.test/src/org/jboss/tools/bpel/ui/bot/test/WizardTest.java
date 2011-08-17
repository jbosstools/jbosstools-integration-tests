package org.jboss.tools.bpel.ui.bot.test;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

import org.jboss.tools.bpel.ui.bot.test.suite.BPELTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

import org.junit.Assert;
import org.junit.Test;

@Require(clearProjects = true, server = @Server(type = ServerType.SOA, state = ServerState.Present), perspective="BPEL")
public class WizardTest extends BPELTest {

	
	@Test
	public void createNewSyncProcess() throws Exception {
		IProject project = createNewProject("SyncProcessProject");
		IFile process =    createNewProcess("SyncProcessProject", "SyncProcess", BPELConstants.SYNC_PROCESS_LABEL, false);
		
		process.getFileExtension();
		String processContent = loadFile(process);
		
		Assert.assertTrue(processContent != null);
		Assert.assertTrue(processContent.contains("http://docs.oasis-open.org/wsbpel/2.0/process/executable"));
		Assert.assertTrue(processContent.contains("<bpel:import location=\"SyncProcessArtifacts.wsdl\""));
		Assert.assertTrue(processContent.contains("<bpel:receive name=\"receiveInput\""));
		Assert.assertTrue(processContent.contains("<bpel:reply name=\"replyOutput\""));
		Assert.assertTrue(isRuntimeSet("SyncProcessProject"));
	}
	
	@Test
	public void createNewAsyncProcess() throws Exception {
		IProject project = createNewProject("AsyncProcessProject");
		IFile process =    createNewProcess("AsyncProcessProject", "AsyncProcess", BPELConstants.ASYNC_PROCESS_LABEL, false);
		
		process.getFileExtension();
		String processContent = loadFile(process);
		
		Assert.assertTrue(processContent != null);
		Assert.assertTrue(processContent.contains("http://docs.oasis-open.org/wsbpel/2.0/process/executable"));
		Assert.assertTrue(processContent.contains("<bpel:import location=\"AsyncProcessArtifacts.wsdl\""));
		Assert.assertTrue(processContent.contains("<bpel:receive name=\"receiveInput\""));
		Assert.assertTrue(processContent.contains("<bpel:invoke name=\"callbackClient\""));
		Assert.assertTrue(isRuntimeSet("AsyncProcessProject"));
	}
	
	@Test
	public void createNewEmptyProcess() throws Exception {
		IProject project = createNewProject("EmptyProcessProject");
		IFile process =    createNewProcess("EmptyProcessProject", "EmptyProcess", BPELConstants.EMPTY_PROCESS_LABEL, false);
		
		process.getFileExtension();
		String processContent = loadFile(process);
		
		Assert.assertTrue(processContent != null);
		Assert.assertTrue(processContent.contains("http://docs.oasis-open.org/wsbpel/2.0/process/executable"));
		Assert.assertTrue(processContent.contains("<bpel:import location=\"EmptyProcessArtifacts.wsdl\""));
		Assert.assertTrue(processContent.contains("<bpel:sequence name=\"main\">"));
		Assert.assertTrue(processContent.contains("<bpel:empty name=\"Empty\"></bpel:empty>"));
		
		Assert.assertFalse(processContent.contains("<bpel:receive name=\"receiveInput\""));
		
		Assert.assertTrue(isRuntimeSet("EmptyProcessProject"));
	}

	@Test
	public void createNewAbstractSyncProcess() throws Exception {
		IProject project = createNewProject("AbstractProcessProject");
		IFile process =    createNewProcess("AbstractProcessProject", "AbstractProcess", BPELConstants.SYNC_PROCESS_LABEL, true);
		
		process.getFileExtension();
		String processContent = loadFile(process);
		
		Assert.assertTrue(processContent != null);
		Assert.assertTrue(processContent.contains("http://docs.oasis-open.org/wsbpel/2.0/process/abstract"));
		Assert.assertTrue(processContent.contains("<bpel:import location=\"AbstractProcessArtifacts.wsdl\""));
		Assert.assertTrue(processContent.contains("<bpel:receive name=\"receiveInput\""));
		Assert.assertTrue(processContent.contains("<bpel:reply name=\"replyOutput\""));
		Assert.assertTrue(isRuntimeSet("AbstractProcessProject"));
	}
	
	
	/**
	 * @author psrna
	 * @throws Exception
	 */
	@Test
	public void createNewDeployDescriptor() throws Exception {
		
		IProject project = createNewProject("ODEProject");
		IFile deploy = createNewDeployDescriptor("ODEProject");
	
		String deployContent = loadFile(deploy);
		Assert.assertTrue(deployContent != null);
		
	}
	
	
	
	boolean isRuntimeSet(String projectName) throws Exception {
		SWTBotView projectExplorer =  bot.viewByTitle("Project Explorer");
		projectExplorer.setFocus();
		
		// diaplay Project Properties
		SWTBotTree tree = projectExplorer.bot().tree().select(projectName);
		tree.getTreeItem(projectName).contextMenu("Properties").click();

		SWTBotShell shell = bot.shell("Properties for " + projectName).activate();
		
		bot.tree().select("Targeted Runtimes");
		bot.checkBox("Show &all runtimes").select();
		boolean hasRuntime = bot.table().containsItem(configuredState.getServer().name); 
		shell.close();
		
		return hasRuntime;
	}
	
	
}
