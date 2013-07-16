package org.jboss.tools.teiid.reddeer.view;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.util.Arrays;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.widget.SWTBotGefFigure;

public class Procedure {

	private String project;

	private String model;

	private String procedure;
	
	private String[] pathToModel;

	public Procedure(String project, String model, String procedure) {
		this.project = project;
		this.model = model;
		this.procedure = procedure;
	}
	
	public Procedure(String... pathToModel){
		this.pathToModel = pathToModel;
	}

	public void addParameter(String name, String type) {
		addParameterName(name);
		addParameterType(name, type);
	}
	
	public void addParameter2(String name, String type) {
		addParameterName2(name);
		addParameterType2(name, type);
	}
	
	/**
	 * 
	 * @param b
	 * @param name of parameter
	 */
	public void addParameter(boolean b, String name){
		updatePathToModel();
		String[] pathToProcedure = Arrays.copyOf(pathToModel, pathToModel.length+1);
		pathToProcedure[pathToProcedure.length-1] = procedure;
		new DefaultTreeItem(pathToProcedure).select();//procedure
		new ContextMenu("New Child", "Procedure Parameter").select();
		
		String[] pathToNewParam = Arrays.copyOf(pathToProcedure, pathToProcedure.length+1);
		pathToNewParam[pathToNewParam.length-1] = "NewProcedureParameter";
		new DefaultTreeItem(pathToNewParam).select();	
		ModelEditor me = new ModelEditor("BooksInfo.xmi");
		me.show();
		ModelExplorerView modelView = TeiidPerspective.getInstance().getModelExplorerView();
		modelView.open();
		new DefaultTreeItem(pathToNewParam).select();
		new ContextMenu("Rename...").select();//highlights text to be edited
		new DefaultText("NewProcedureParameter").setText(name);
		new DefaultTreeItem(pathToModel).select();//click somewhere else
		//me.save();
	}
	
	public void addParameterType(String parameterName, String type, boolean b){
		updatePathToModel();
		String[] path = Arrays.copyOf(pathToModel, pathToModel.length+2);
		path[path.length-2]= procedure;
		path[path.length-1] = parameterName;
		
		new DefaultTreeItem(path).select();
		new ContextMenu("Modeling", "Set Datatype").select();

		Shell shell = new DefaultShell("Select a Datatype");
		Bot.get().table().getTableItem(type).select();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	private void addParameterName(String parameter) {
		new DefaultTreeItem(project, model, procedure).select();
		new ContextMenu("New Child", "Procedure Parameter").select();
		new LabeledText("NewProcedureParameter").setText(parameter);

		// bot.waitUntil(new TreeContainsNode(bot.tree(), project, model,
		// procedure, parameter), TaskDuration.NORMAL.getTimeout());
	}
	
	private void addParameterName2(String parameter) {
		new DefaultTreeItem(project, model, procedure).select();
		new ContextMenu("New Child", "Procedure Parameter").select();
		new DefaultTreeItem(project, model, procedure, "NewProcedureParameter").select();	
		new ContextMenu("Rename...").select();//highlights text to be edited
		new DefaultText("NewProcedureParameter").setText(parameter);
			}

	private void addParameterType(String parameter, String type) {
		new DefaultTreeItem(project, model, procedure).select();
		new ContextMenu("Modeling", "Set Datatype").select();

		Shell shell = new DefaultShell("Select a Datatype");
		Bot.get().table().getTableItem(type).select();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private void addParameterType2(String parameter, String type) {
		new DefaultTreeItem(project, model, procedure, parameter).select();
		new ContextMenu("Modeling", "Set Datatype").select();

		Shell shell = new DefaultShell("Select a Datatype");
		Bot.get().table().getTableItem(type).select();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	/**
	 * Creates a procedure
	 * @param name of procedure to be created
	 * @param procedure - true if procedure is created, false if function
	 */
	public void create(String name, boolean procedure) {
		updatePathToModel();
		this.procedure = name;
		
		new DefaultTreeItem(pathToModel).select();
		new ContextMenu("New Child", "Procedure...").select();
		if (procedure){
			new PushButton("OK").click();
		}
		new LabeledText("Name").setText(name);
		new PushButton("OK").click();
	}

	private void updatePathToModel() {
		if (pathToModel == null){
			pathToModel = new String[2];
			pathToModel[0] = this.project;
			pathToModel[1] = this.model;
		}
	}
}
