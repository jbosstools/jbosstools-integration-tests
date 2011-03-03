package org.jboss.tools.bpel.ui.bot.test.suite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

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
		tree.expandNode("BPEL 2.0").expandNode("New BPEL Process File").select();
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
	    bot.sleep(5000);
	    
	    IProject iproject = ResourcesPlugin.getWorkspace().getRoot().getProject(project);
	    IFile bpelFile = iproject.getFile(new Path("bpelContent/" + name + ".bpel"));
	    assertNotNull(bpelFile);
	    assertNotNull(iproject.getFile(new Path("bpelContent/" + name + ".bpelex")));
	    assertNotNull(iproject.getFile(new Path("bpelContent/" + name + "Artifacts.wsdl")));
	    
	    return bpelFile;
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
}
