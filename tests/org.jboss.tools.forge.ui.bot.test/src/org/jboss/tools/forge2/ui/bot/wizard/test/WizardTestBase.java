package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
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
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.closeeditors.CloseAllEditorsRequirement.CloseAllEditors;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.forge.reddeer.view.ForgeConsoleView;
import org.jboss.tools.forge.ui.bot.test.util.ScaffoldType;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
/**
 * Base class for Forge2 wizard tests
 * @author Pavol Srna
 *
 */
@RunWith(RedDeerSuite.class)
@CloseAllEditors
public abstract class WizardTestBase {

	protected static final String PROJECT_NAME = "testProject";
	protected static final String WORKSPACE = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	protected static final String GROUPID = "org.jboss.tools.forge.test";
	
	@BeforeClass
	public static void setup(){
		ForgeConsoleView view = new ForgeConsoleView();
		view.start();
	}
	
	@After
	public void cleanup(){
		handleCleanup();
		assertTrue("Some of the resources have not been deleted!", new ProjectExplorer().getExplorerItems().isEmpty());
	}
	
	public void handleCleanup(){
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
		if(!new ProjectExplorer().getExplorerItems().isEmpty()){
			//workaround for windows issue - JDK_8029516
			new ForgeConsoleView().stop();
			System.gc();
			new ProjectExplorer().deleteAllProjects();
			new ForgeConsoleView().start();
		}
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
	
	/**
	 * This method executes forge command specified by name and waits
	 * until appropriate wizard shell (matched by regex) appears in UI.
	 * 
	 * @param command - forge command name to be executed
	 * @param title_regex - regex to wait for correct wizard shell
	 * @return WizardDialog
	 */
	public WizardDialog getWizardDialog(String command, String title_regex){
		runForgeCommand(command);
		RegexMatcher rm = new RegexMatcher(title_regex);
		new WaitUntil(new ShellWithTextIsActive(rm));
		return new WizardDialog();
	}
	
	/**
	 * This method creates new project specified by name and path 
	 * and verifies if project is imported in eclipse.
	 * Forge 'project-new' command will be used for project creation.
	 * 
	 * @param name of the project
	 * @param path 
	 */
	public void newProject(String name, String path){		
		WizardDialog wd = getWizardDialog("project-new", "(Project: New).*");
		new LabeledText("Project name:").typeText(name);
		new LabeledText("Project location:").setText(path);
		wd.finish(TimePeriod.getCustom(600));
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue(pe.containsProject(name));
	}
	
	/**
	 * Creates new project in currect workspace.
	 * For more details @see WizardTestBase#newProject(String name, String path)
	 * @param name
	 */
	public void newProject(String name){
		newProject(name, WORKSPACE);
	}
	
	/**
	 * Runs jpa-setup wizard with default values on specified project
	 * @param projectName 
	 */
	public void persistenceSetup(String projectName){
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(projectName); //this will set context for forge
		WizardDialog wd = getWizardDialog("jpa-setup", "(JPA: Setup).*");
		wd.finish();
		File persistence = new File(WORKSPACE + "/" + projectName + "/src/main/resources/META-INF/persistence.xml");
		assertTrue("persistence.xml file does not exist", persistence.exists());
	}
	
	/**
	 * Runs servlet-setup wizard on specified project
	 * @param projectName
	 * @param webFacetVersion to be set
	 */
	public void servletSetup(String projectName, String webFacetVersion){
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(projectName); //this will set context for forge
		WizardDialog wd = getWizardDialog("servlet-setup", "(Servlet: Setup).*");
		new LabeledCombo("Servlet Version:").setSelection(webFacetVersion);
		wd.finish(TimePeriod.getCustom(600));
	}
	
	/**
	 * Runs cdi-setup wizard on specified project
	 * @param projectName
	 * @param cdiVersion to be set
	 */
	public void cdiSetup(String projectName, String cdiVersion){
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(projectName); //this will set context for forge
		WizardDialog wd = getWizardDialog("cdi-setup", "(CDI: Setup).*");
		new LabeledCombo("CDI Version:").setSelection(cdiVersion);
		wd.finish(TimePeriod.getCustom(600));
	}
	
	/**
	 * Runs faces-setup wizard on specified project
	 * @param projectName
	 * @param jsfVersion to be set
	 */
	public void facesSetup(String projectName, String jsfVersion){
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(projectName); //this will set context for forge
		WizardDialog wd = getWizardDialog("faces-setup", "(Faces: Setup).*");
		new LabeledCombo("JavaServer Faces Version:").setSelection(jsfVersion);
		wd.finish(TimePeriod.getCustom(600));
	}

	/**
	 * Creates a new JPA entity in a given project with a set name, table name and package
	 * @param projectName
	 * @param entityName
	 * @param tableName
	 * @param pkg package to be set
	 */
	public void newJPAEntity(String projectName, String entityName, String tableName, String pkg) {
		new ProjectExplorer().selectProjects(projectName);
		WizardDialog dialog = getWizardDialog("JPA: New Entity", "(JPA: New Entity).*");
		new LabeledText("Package Name:").setText(pkg);
		new LabeledText("Type Name:").setText(entityName);
		new LabeledText("Table Name:").setText(tableName);
		dialog.finish(TimePeriod.LONG);	
	}
	
	/**
	 * Runs scaffold setup wizard on a specified project
	 * @param projectName
	 * @param type Type of the scaffold to be used
	 */
	public void scaffoldSetup(String projectName, ScaffoldType type) {
		new ProjectExplorer().selectProjects(projectName);
		WizardDialog dialog = getWizardDialog("Scaffold: Setup", "(Scaffold: Setup).*");
		new DefaultCombo().setSelection(type.getName());
		new WaitUntil(new ButtonWithTextIsEnabled(new PushButton("Next >")));
		dialog.next();
		dialog.finish(TimePeriod.LONG);
	}
}
