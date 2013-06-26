package org.jboss.tools.teiid.reddeer.view;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * Represent Model Explorer View and equivalent to Package explorer from Java
 * perspective.
 * 
 * @author Lucia Jelinkova
 * 
 */
public class ModelExplorerView extends WorkbenchView {

	private static final String MODELING_MENU_ITEM = "Modeling";

	public ModelExplorerView() {
		super("Teiid Designer", "Model Explorer");
	}

	public void newBaseTable(String project, String model, String tableName) {
		open();

		new DefaultTreeItem(project, model).select();
		new ContextMenu("New Child", "Table...").select();
		new DefaultShell("Create Relational Table");
		new LabeledText("Name").setText(tableName);
		new PushButton("OK").click();
	}
	
	/**
	 * Create new (base) table in view model
	 * @param project
	 * @param model
	 * @param tableName
	 * @param baseTable true if context menu contains "Base Table"
	 */
	public void newBaseTable(String project, String model, String tableName, boolean baseTable) {
		open();

		new DefaultTreeItem(project, model).select();
		if (baseTable){
			new ContextMenu("New Child", "Base Table...").select();
		} else {
			new ContextMenu("New Child", "Table...").select();
		}
		
		new DefaultShell("Create Relational Table");
		new LabeledText("Name").setText(tableName);
		new PushButton("OK").click();
	}

	public Procedure newProcedure(String project, String model, String procedure) {
		open();

		new DefaultTreeItem(project, model).select();
		new ContextMenu("New Child", "Procedure...").select();
		new LabeledText("NewProcedure").setText(procedure);

		return new Procedure(project, model, procedure);
	}
	
	public Procedure newProcedure(String project, String model, String procedure, boolean procedureNotFunction) {
		open();

		new DefaultTreeItem(project, model).select();
		new ContextMenu("New Child", "Procedure...").select();
		
		if (procedureNotFunction){
			//Procedure?/(Function) - OK
			Bot.get().button("OK").click();
		}
		
		new LabeledText("Name").setText(procedure);
		
		//finish
		Bot.get().button("OK").click();

		return new Procedure(project, model, procedure);
	}

	public void addTransformationSource(String project, String model, String tableName) {
		open();
		
		new DefaultTreeItem(project, model, tableName).select();
		new ContextMenu(MODELING_MENU_ITEM, "Add Transformation Source(s)").select();
	}

	public void executeVDB(String project, String vdb) {
		open();

		new DefaultTreeItem(project, vdb).select();
		new ContextMenu(MODELING_MENU_ITEM, "Execute VDB").select();

		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public void deployVDB(String project, String vdb) {
		open();

		new DefaultTreeItem(project, vdb).select();
		new ContextMenu(MODELING_MENU_ITEM, "Deploy").select();

		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public void open(String... filePath) {
		open();

		Bot.get().tree(0).expandNode(filePath).doubleClick();
	}

	public void openTransformationDiagram(String... filePath) {
		open();

		SWTBotTreeItem item = Bot.get().tree(0).expandNode(filePath);

		item.expand();
		item.getNode("Transformation Diagram").doubleClick();
	}
}
