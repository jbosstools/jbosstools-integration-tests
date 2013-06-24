package org.jboss.tools.teiid.reddeer.view;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

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
import org.jboss.tools.teiid.reddeer.widget.SWTBotGefFigure;

public class Procedure {

	private String project;

	private String model;

	private String procedure;

	public Procedure(String project, String model, String procedure) {
		this.project = project;
		this.model = model;
		this.procedure = procedure;
	}

	public void addParameter(String name, String type) {
		addParameterName(name);
		addParameterType(name, type);
	}
	
	public void addParameter2(String name, String type) {
		addParameterName2(name);
		addParameterType2(name, type);
	}

	private void addParameterName(String parameter) {
		new DefaultTreeItem(project, model, procedure).select();
		new ContextMenu("New Child", "Procedure Parameter").select();
		new LabeledText("NewProcedureParameter").setText(parameter);

		// bot.waitUntil(new TreeContainsNode(bot.tree(), project, model,
		// procedure, parameter), TaskDuration.NORMAL.getTimeout());
	}
	
	private void addParameterName2(String parameter) {
		System.out.println(Bot.get().tree(0).columnCount());//0
		/*ModelExplorerView mev = new ModelExplorerView();
		mev.open();*/
		new DefaultTreeItem(project, model, procedure).select();
		new ContextMenu("New Child", "Procedure Parameter").select();
		new DefaultTreeItem(project, model, procedure, "NewProcedureParameter").select();
		
		//System.out.println(new DefaultTree(2).getAllItems().size() + ", " + new DefaultTree(2).getItems().size());
		new ContextMenu("Rename...").select();//highlights text to be edited
		//but how to edit it???
		
		
		new DefaultText("NewProcedureParameter").setText(parameter);
		System.out.println();
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
}
