package org.jboss.tools.bpel.ui.bot.test.suite;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.bpel.ui.bot.test.AssignActivityTest;
import org.jboss.tools.bpel.ui.bot.test.OdeDeployTest;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;

public class BPELTest extends SWTTestExt {

	public static void prepare() {
		log.info("BPEL All Test started...");
		
//		jbt.closeReportUsageWindowIfOpened(true);
//		eclipse.maximizeActiveShell();
//		eclipse.closeView(IDELabel.View.WELCOME);
//		bot.closeAllEditors();		
	}

	public static void clean() {
		util.waitForNonIgnoredJobs();
		bot.sleep(TIME_5S, "BPEL All Tests Finished!");
	}
	
	
	
	protected ProjectExplorer projExplorer = new ProjectExplorer(){
		
		@Override
		public void runOnServer(String projectName) {
			String serverName = AssignActivityTest.configuredState.getServer().name;
			//serverName = "SOA-5.1";

			bot.viewByTitle("Servers").show();
			bot.viewByTitle("Servers").setFocus();
			
			SWTBotTree tree = bot.viewByTitle("Servers").bot().tree(); 
			bot.sleep(TIME_5S);
			SWTBotTreeItem server = tree.getTreeItem(serverName + "  [Started, Synchronized]").select();
			
			ContextMenuHelper.prepareTreeItemForContextMenu(tree, server);
			new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.ADD_AND_REMOVE, false)).click();
			
			SWTBotShell shell = OdeDeployTest.bot.shell("Add and Remove...");
			shell.activate();
			
			SWTBot viewBot = shell.bot();
			viewBot.tree().setFocus();
			viewBot.tree().select(projectName);
			viewBot.button("Add >").click();
			viewBot.button("Finish").click();
		}
	};
	
	

	/**
	 * Creates a new process in a project identified by it's name.
	 * 
	 * TODO: extend WSDL validation
	 * 
	 * @param project	 project name in which to create the new process
	 * @param name		 process name
	 * @param type		 process type (sync, async, empty)
	 * @param isAbstract is the process supposed to be abstract?
	 * 
	 * @return			 process file
	 */
	protected IFile createNewProcess(String project, String name, String type, boolean isAbstract) {
		SWTBotView view = bot.viewByTitle("Project Explorer");
		view.show();
		view.setFocus();
		
		SWTBot viewBot = view.bot();
		SWTBotTreeItem item = viewBot.tree().expandNode(project).expandNode("bpelContent");
		item.select();
		
		bot.menu("File").menu("New").menu("Other...").click();
		bot.shell("New").activate();
		
		SWTBotTree tree  = bot.tree();
		tree.expandNode("BPEL 2.0").expandNode("BPEL Process File").select();
		//tree.expandNode("BPEL 2.0").expandNode("New BPEL Process File").select();
	    assertTrue(bot.button("Next >").isEnabled());
	    
	    bot.button("Next >").click();
	    assertFalse(bot.button("Next >").isEnabled());
	    
	    bot.textWithLabel("BPEL Process Name:").setText(name); 
	    bot.comboBoxWithLabel("Namespace:").setText("http://eclipse.org/bpel/sample"); 
	    bot.comboBoxWithLabel("Template:").setSelection(type + " BPEL Process"); 
	    if(isAbstract) {
	    	bot.checkBox().select();
	    } else {
	    	bot.checkBox().deselect();
	    	assertTrue(bot.button("Next >").isEnabled());
	 	   
		    bot.button("Next >").click();
		    assertEquals(name, bot.textWithLabel("Service Name").getText());
	    }
	    
	    bot.button("Finish").click();
	    bot.sleep(TIME_5S);
	    
	    IProject iproject = ResourcesPlugin.getWorkspace().getRoot().getProject(project);
	    IFile bpelFile = iproject.getFile(new Path("bpelContent/" + name + ".bpel"));
	    assertTrue(bpelFile.exists());
	    //assertTrue(iproject.getFile(new Path("bpelContent/" + name + ".bpelex")).exists());
	    assertTrue(iproject.getFile(new Path("bpelContent/" + name + "Artifacts.wsdl")).exists());
	    
	    return bpelFile;
	}
	
	/**
	 * Creates a new ODE deployment descriptor in a project identified by it's name.
	 * 
	 * @author psrna
	 * 
	 * @param project	project name in which to create the new ODE deployment descriptor
	 * @return			deployment descriptor file
	 */
	protected IFile createNewDeployDescriptor(String project){
		
		SWTBotView view = bot.viewByTitle("Project Explorer");
		view.show();
		view.setFocus();
		
		SWTBot viewBot = view.bot();
		SWTBotTreeItem item = viewBot.tree().expandNode(project).expandNode("bpelContent");
		item.select();
		
		bot.menu("File").menu("New").menu("Other...").click();
		bot.shell("New").activate();
		
		SWTBotTree tree  = bot.tree();
		tree.expandNode("BPEL 2.0").expandNode("BPEL Deployment Descriptor").select();
		//tree.expandNode("BPEL 2.0").expandNode("Apache ODE Deployment Descriptor").select();
	    assertTrue(bot.button("Next >").isEnabled());
	    
	    bot.button("Next >").click();

	    assertTrue(bot.textWithLabel("BPEL Project:").getText().equals("/" + project + "/bpelContent"));
	    assertTrue(bot.textWithLabel("File name:").getText().equals("deploy.xml"));
	    
	    bot.button("Finish").click();
	    bot.sleep(5000);
	    
	    IProject iproject = ResourcesPlugin.getWorkspace().getRoot().getProject(project);
	    IFile deployFile = iproject.getFile(new Path("bpelContent/deploy.xml"));
	    assertTrue(deployFile.exists());

		return deployFile;
	}
	
	
	/**
	 * Create a new BPEL project
	 * @param name project name
	 * @return     project reference
	 */
	protected IProject createNewProject(String name) {
		SWTBotView view = bot.viewByTitle("Project Explorer");
		view.show();
		view.setFocus();
		
		bot.menu("File").menu("New").menu("Project...").click();
		bot.shell("New Project").activate();
		
		SWTBotTree tree  = bot.tree();
		tree.expandNode("BPEL 2.0").expandNode("BPEL Project").select();
	    assertTrue(bot.button("Next >").isEnabled());
	    
	    bot.button("Next >").click();
	    bot.shell("New BPEL Project").activate();
	    assertFalse(bot.button("Finish").isEnabled());
	    
	    bot.textWithLabel("Project name:").setText(name); 
	    assertTrue(bot.button("Finish").isEnabled());
	    
	    bot.button("Finish").click();
	    bot.sleep(3000);
	    
	    IProject iproject = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	    assertNotNull(iproject);
	    
	    return iproject;
	}
	
	public String loadFile(IFile file) throws Exception {
		if(file.getType() != IFile.FILE) {
			throw new IllegalArgumentException("File: " + file.getFullPath().toString() + " is a directory!");
		}
		
		InputStream in = null;
		StringBuffer out;
		try {
			in = file.getContents();
			out = new StringBuffer();
			byte[] buffer = new byte[4 * 1024];
			int c = 0;
			while((c = in.read(buffer)) > -1) {
				out.append(new String(buffer, 0, c));
			}
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return out.length() == 0 ? null : out.toString();
	}
	
}
