package org.jboss.tools.teiid.reddeer.view;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

public class GuidesView {

	private int ACTION_SETS_TREE_INDEX = 2;//TODO hardcoded index; 0 - model explorer view, 1 - model actions, 2 - other 
	
	/**
	 * Choose specific action from action set.
	 * @param actionSet 
	 * @param action
	 */
	public void chooseAction(String actionSet, String action){
		new SWTWorkbenchBot().cTabItem("Guides").activate();
		new SWTWorkbenchBot().comboBox().setSelection(actionSet);
		SWTBotTree t = new SWTWorkbenchBot().tree(ACTION_SETS_TREE_INDEX).select(action);//index hardcoded!
		t.getTreeItem(action).doubleClick();
	}
	
	/**
	 * Preview data (table) via guides action
	 * @param calledFirstTime true - sets up display property of unresolvable SQL results
	 * @param path to table (e.g. "ProjectName", "ModelName", "TABLE")
	 */
	public void previewData(boolean calledFirstTime, String... path){
		new GuidesView().chooseAction("Model JDBC Source", "Preview Data");
		new SWTWorkbenchBot().button("...").click();
		new DefaultTreeItem(path).select();
		new SWTWorkbenchBot().button("OK").click();
		new SWTWorkbenchBot().button("OK").click();
		
		//setup display property; only 1st time
		try {
			//what property?
			new SWTWorkbenchBot().activeShell().bot().button("Yes").click();
		} catch (Exception ex){
			//do nothing
		}
		
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
	}
}
