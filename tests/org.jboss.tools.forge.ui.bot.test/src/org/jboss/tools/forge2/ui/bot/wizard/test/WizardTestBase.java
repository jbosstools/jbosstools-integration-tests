package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.services.IServiceLocator;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.forge.reddeer.view.ForgeConsoleView;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public abstract class WizardTestBase {

	protected static final String WORKSPACE = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	
	@BeforeClass
	public static void setup(){
		ForgeConsoleView view = new ForgeConsoleView();
		view.selectRuntime(new RegexMatcher("Forge 2.*"));
		view.start();
	}
	
	@After
	public void cleanup(){
		new ProjectExplorer().deleteAllProjects();
	}
	
	/**
	 * Run Forge Command
	 * 
	 * @param params is HashMap accepting 3 different parameters:
	 * - org.jboss.tools.forge.ui.runForgeCommand.commandName : required
	 * - org.jboss.tools.forge.ui.runForgeCommand.commandTitle : optional
	 * - org.jboss.tools.forge.ui.runForgeCommand.commandValues : optional
	 * @throws Exception if required parameter is not specified
	 */
	public void runForgeCommand(HashMap<String, String> params) throws Exception{
		if(params.get("org.jboss.tools.forge.ui.runForgeCommand.commandName") == null){
			throw new Exception("Parameter: 'org.jboss.tools.forge.ui.runForgeCommand.commandName' is required!");
		}
		
		IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);

		Command command = commandService.getCommand("org.jboss.tools.forge.ui.runForgeCommand");
	    final ParameterizedCommand pcommand = ParameterizedCommand.generateCommand(command, params);
	    
    	Display.asyncExec(new Runnable() {	
			@Override
			public void run() {
				try {
					pcommand.executeWithChecks(new ExecutionEvent(), null);					
				} catch (ExecutionException | NotDefinedException |
						 NotEnabledException | NotHandledException e) {
					// Replace with real-world exception handling
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Run Forge Command specified by the commandName.
	 * e.g 'project-new' will open Project New wizard
	 * @param commandName
	 */
	public void runForgeCommand(String commandName){
		
	    HashMap<String, String> params = new HashMap<String, String>();
	    params.put("org.jboss.tools.forge.ui.runForgeCommand.commandName", commandName);
	    try{
	    	this.runForgeCommand(params);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	
	public WizardDialog getWizardDialog(String command, String title_regex){
		runForgeCommand(command);
		RegexMatcher rm = new RegexMatcher(title_regex);
		new WaitUntil(new ShellWithTextIsActive(rm));
		return new WizardDialog();
	}
	
	public void newProject(String name, String path){		
		WizardDialog wd = getWizardDialog("project-new", "(Project: New).*");
		new LabeledText("Project name:").setText(name);
		new LabeledText("Project location:").setText(path);
		wd.finish();
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue(pe.containsProject(name));
	}
	
	public void newProject(String name){
		newProject(name, WORKSPACE);
	}
	
}
