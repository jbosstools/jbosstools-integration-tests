package org.jboss.tools.teiid.reddeer.view;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

public class SQLResult {

	public static final String STATUS_SUCCEEDED = "Succeeded";
	
	private SWTBotTreeItem resultRow;
	
	public SQLResult(SWTBotTreeItem resultRow) {
		this.resultRow = resultRow;
	}

	public String getStatus(){
		return resultRow.cell(0);
	}
	
	public int getCount(){
		new SWTWorkbenchBot().cTabItem("Result1").activate();
		return new SWTWorkbenchBot().table().rowCount();
	}
	
}
